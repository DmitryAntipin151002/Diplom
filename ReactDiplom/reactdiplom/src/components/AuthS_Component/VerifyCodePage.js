import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import '../../assets/AuthS/VerifyCodePage.css';

const VerifyCodePage = () => {
    const [code, setCode] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const { state } = useLocation();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (code.length !== 6) {
            setError('Код должен содержать 6 цифр');
            return;
        }
        setIsLoading(true);

        try {
            const response = await AuthService.verifyCode(
                state.userId,
                code,
                state.tempToken
            );

            localStorage.setItem('isFirstEnterToken', response.data.isFirstEnterToken);
            navigate('/dashboard');
        } catch (err) {
            setError('Неверный код подтверждения');
        } finally {
            setIsLoading(false);
        }
    };

    const handleResendCode = async () => {
        try {
            await AuthService.resendCode(state.userId, state.tempToken);
            alert('Новый код отправлен!');
            setError('');
        } catch (err) {
            setError('Ошибка при повторной отправке кода');
        }
    };

    return (
        <div className="verify-code-container">
            <div className="verify-code-glass">
                <div className="verify-code-header">
                    <h2>Подтверждение входа 🔒</h2>
                    <p className="subtitle">Введите код из SMS или Email</p>
                </div>

                <form onSubmit={handleSubmit}>
                    <div className="code-input-group">
                        <input
                            type="text"
                            inputMode="numeric"
                            pattern="[0-9]*"
                            placeholder="000000"
                            value={code}
                            onChange={(e) => {
                                const value = e.target.value.replace(/\D/g, '');
                                setCode(value.slice(0, 6));
                                setError('');
                            }}
                            className="code-input"
                            autoFocus
                        />
                    </div>

                    <button
                        type="submit"
                        className="verify-button"
                        disabled={isLoading}
                    >
                        {isLoading ? (
                            <div className="spinner"></div>
                        ) : (
                            'Подтвердить'
                        )}
                    </button>

                    {error && <div className="error-message">{error}</div>}

                    <div className="resend-section">
                        <p>Не получили код?</p>
                        <button
                            type="button"
                            onClick={handleResendCode}
                            className="resend-button"
                        >
                            Отправить код повторно
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default VerifyCodePage;