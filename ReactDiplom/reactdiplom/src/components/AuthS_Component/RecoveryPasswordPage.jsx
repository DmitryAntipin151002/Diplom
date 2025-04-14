import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import '../../assets/AuthS/RecoveryPage.css';

const RecoveryPasswordPage = () => {
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const { state } = useLocation();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (newPassword !== confirmPassword) {
            setError('Пароли не совпадают');
            return;
        }

        if (newPassword.length < 8) {
            setError('Пароль должен быть минимум 8 символов');
            return;
        }

        setIsLoading(true);
        try {
            await AuthService.updatePassword(
                state.recoveryToken,
                { newPassword }
            );

            navigate('/login', {
                state: {
                    successMessage: '🎉 Пароль успешно изменен!',
                    successType: 'passwordChanged'
                }
            });

        } catch (err) {
            setError(err.response?.data?.message || 'Ошибка при смене пароля');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="recovery-container">
            <div className="auth-glass">
                <div className="password-header">
                    <h2 className="gradient-text">Новый пароль</h2>
                    <p className="subtitle">Придумайте надежный пароль для вашего аккаунта</p>
                </div>

                <form onSubmit={handleSubmit} className="auth-form">
                    <div className="input-group password-input">
                        <input
                            type={showPassword ? "text" : "password"}
                            placeholder=" "
                            value={newPassword}
                            onChange={(e) => {
                                setNewPassword(e.target.value);
                                setError('');
                            }}
                        />
                        <label>Новый пароль</label>
                        <button
                            type="button"
                            className="toggle-password"
                            onClick={() => setShowPassword(!showPassword)}
                        >
                            {showPassword ? '👁️' : '👁️'}
                        </button>
                    </div>

                    <div className="input-group">
                        <input
                            type="password"
                            placeholder=" "
                            value={confirmPassword}
                            onChange={(e) => {
                                setConfirmPassword(e.target.value);
                                setError('');
                            }}
                        />
                        <label>Повторите пароль</label>
                    </div>

                    {error && (
                        <div className="alert alert-error">
                            ⚠️ {error}
                            <button
                                className="close-btn"
                                onClick={() => setError('')}
                            >
                                ✖
                            </button>
                        </div>
                    )}

                    <button
                        type="submit"
                        className="auth-button save-button"
                        disabled={isLoading}
                    >
                        {isLoading ? (
                            <div className="spinner"></div>
                        ) : (
                            <>
                                <span>Сохранить изменения</span>
                                <svg className="icon-arrow" viewBox="0 0 24 24">
                                    <path d="M4 12h16m-7-7l7 7-7 7"/>
                                </svg>
                            </>
                        )}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default RecoveryPasswordPage;