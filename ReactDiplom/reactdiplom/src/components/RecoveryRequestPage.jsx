import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../services/AuthService';
import './../assets/RecoveryPage.css';

const RecoveryRequestPage = () => {
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Формируем объект данных
        const requestData = {};
        if (email) requestData.email = email;
        if (phone) requestData.phoneNumber = phone;

        if (Object.keys(requestData).length === 0) {
            setError('Заполните email и телефон');
            return;
        }

        setIsLoading(true);
        try {
            const response = await AuthService.recoverPassword(requestData);
            console.log('Ответ получен:', response);

            // Добавьте проверку наличия токена
            if (!response?.recoveryToken) {
                throw new Error('Неверный формат ответа сервера');
            }

            navigate('/recovery/password', {
                state: {
                    recoveryToken: response.recoveryToken
                }
            });

        } catch (err) {
            console.error('Полная ошибка:', err);
            setError(err.message);
        }
    };

    return (
        <div className="recovery-container">
            <div className="auth-glass">
                <h2 className="gradient-text">Восстановление пароля</h2>
                <p className="subtitle">Введите email и телефон</p>

                <form onSubmit={handleSubmit} className="auth-form">
                    <div className="input-group">
                        <input
                            type="email"
                            placeholder="Email"
                            value={email}
                            onChange={(e) => {
                                setEmail(e.target.value.trim());
                                setError('');
                            }}
                        />
                    </div>

                    <div className="divider">И</div>

                    <div className="input-group">
                        <input
                            type="tel"
                            placeholder="Телефон (+71234567890)"
                            value={phone}
                            onChange={(e) => {
                                const value = e.target.value
                                    .replace(/\D/g, '')
                                    .replace(/^(\d)/, '+$1')
                                    .slice(0, 12);
                                setPhone(value);
                                setError('');
                            }}
                        />
                    </div>

                    {error && (
                        <div className="alert alert-error">
                            {error}
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
                        className="auth-button"
                        disabled={isLoading}
                    >
                        {isLoading ? (
                            <div className="spinner"></div>
                        ) : (
                            'Продолжить'
                        )}
                    </button>
                </form>
            </div>
        </div>
    );
};

export default RecoveryRequestPage;