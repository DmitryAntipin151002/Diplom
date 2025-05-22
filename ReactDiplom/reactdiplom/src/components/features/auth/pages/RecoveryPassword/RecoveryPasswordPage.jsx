import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import AuthService from '../../../auth/services/AuthService';
import PasswordForm from './PasswordForm';
import '../../../auth/styles/RecoveryPage.css';

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
            await AuthService.updatePassword(state.recoveryToken, { newPassword });
            navigate('/login', {
                state: {
                    successMessage: '🎉 Пароль успешно изменен!',
                    successType: 'passwordChanged',
                },
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
                <PasswordForm
                    newPassword={newPassword}
                    confirmPassword={confirmPassword}
                    setNewPassword={setNewPassword}
                    setConfirmPassword={setConfirmPassword}
                    showPassword={showPassword}
                    setShowPassword={setShowPassword}
                    handleSubmit={handleSubmit}
                    error={error}
                    setError={setError}
                    isLoading={isLoading}
                />
            </div>
        </div>
    );
};

export default RecoveryPasswordPage;
