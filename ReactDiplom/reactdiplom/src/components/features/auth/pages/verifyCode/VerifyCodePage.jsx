import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import VerifyCodeHeader from './VerifyCodeHeader';
import VerifyCodeForm from './VerifyCodeForm';
import '../../styles/VerifyCodePage.css';

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
                <VerifyCodeHeader />
                <VerifyCodeForm
                    code={code}
                    setCode={setCode}
                    error={error}
                    isLoading={isLoading}
                    handleSubmit={handleSubmit}
                    handleResendCode={handleResendCode}
                />
            </div>
        </div>
    );
};

export default VerifyCodePage;
