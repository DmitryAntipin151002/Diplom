import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../services/AuthService";
import "../assets/LoginPage.css";

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            // 1. Делаем запрос через AuthService
            const response = await AuthService.login({ email, password });

            // 2. Логируем полный ответ для отладки
            console.log('Full response:', response);
            console.log('Response data:', response.data);

            // 3. Проверяем наличие данных ответа
            if (!response || !response.data) {
                throw new Error('Пустой ответ от сервера');
            }

            // 4. Проверяем обязательные поля с защитой от undefined
            if (response.data.preAuthorizationToken && response.data.id) {
                navigate('/verify-code', {
                    state: {
                        userId: response.data.id,
                        tempToken: response.data.preAuthorizationToken
                    }
                });
            } else {
                // 5. Обрабатываем случай отсутствия обязательных полей
                const missingFields = [];
                if (!response.data.id) missingFields.push('id');
                if (!response.data.preAuthorizationToken) missingFields.push('preAuthorizationToken');

                throw new Error(`Отсутствуют обязательные поля: ${missingFields.join(', ')}`);
            }

        } catch (err) {
            // 6. Улучшенная обработка ошибок
            console.error('Error details:', {
                message: err.message,
                response: err.response,
                stack: err.stack
            });

            // 7. Формируем понятное сообщение об ошибке
            const errorMessage = err.response?.data?.message
                || err.message
                || "Неизвестная ошибка авторизации";

            setError(errorMessage);
            setTimeout(() => setError(''), 5000);

        } finally {
            setLoading(false);
        }
    };



    const handleCloseAlert = () => {
        setError('');
        setSuccessMessage('');
    };

    return (
        <div className="auth-container">
            <div className="auth-glass">
                {error && (
                    <div className="alert alert-error slide-in">
                        {error}
                        <button className="close-btn" onClick={handleCloseAlert}>✖</button>
                    </div>
                )}
                {successMessage && (
                    <div className="alert alert-success slide-in">
                        {successMessage}
                        <button className="close-btn" onClick={handleCloseAlert}>✖</button>
                    </div>
                )}

                <div className="auth-content">
                    <div className="auth-header">
                        <img
                            src="https://storage.googleapis.com/tagjs-prod.appspot.com/v1/RC5xlerM5u/mej84c5v_expires_30_days.png"
                            alt="Логотип"
                            className="logo-float"
                        />
                        <h1 className="gradient-text">С возвращением!</h1>
                        <p className="subtitle">Войдите в свой аккаунт</p>
                    </div>

                    <form onSubmit={handleLogin} className="auth-form">
                        <div className="input-group">
                            <input
                                type="email"
                                placeholder=" "
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="floating-input"
                                required
                            />
                            <label className="floating-label">Email</label>
                        </div>

                        <div className="input-group">
                            <input
                                type="password"
                                placeholder=" "
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="floating-input"
                                required
                            />
                            <label className="floating-label">Пароль</label>
                        </div>

                        <div className="auth-options">
                            <label className="remember-me">
                                <input type="checkbox" />
                                <span className="checkmark"></span>
                                Запомнить меня
                            </label>
                            <a href="/recovery" className="forgot-password">
                                Забыли пароль?
                            </a>
                        </div>

                        <button
                            type="submit"
                            className="auth-button"
                            disabled={loading}
                        >
                            {loading ? (
                                <div className="spinner"></div>
                            ) : (
                                <>
                                    <span>Войти</span>
                                    <span className="arrow">→</span>
                                </>
                            )}
                        </button>

                        <div className="social-auth">
                            <p className="divider">Или войдите через</p>
                            <div className="social-buttons">
                                <button type="button" className="social-btn google">
                                    <img
                                        src="https://storage.googleapis.com/tagjs-prod.appspot.com/v1/RC5xlerM5u/e8deon3h_expires_30_days.png"
                                        alt="Google"
                                    />
                                </button>
                                <button type="button" className="social-btn facebook">
                                    <img
                                        src="https://storage.googleapis.com/tagjs-prod.appspot.com/v1/RC5xlerM5u/42vskjdn_expires_30_days.png"
                                        alt="Facebook"
                                    />
                                </button>
                                <button type="button" className="social-btn twitter">
                                    <img
                                        src="https://storage.googleapis.com/tagjs-prod.appspot.com/v1/RC5xlerM5u/82a2relp_expires_30_days.png"
                                        alt="Twitter"
                                    />
                                </button>
                            </div>
                        </div>

                        <p className="auth-footer">
                            Нет аккаунта?
                            <a href="/register" className="link">Зарегистрироваться</a>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;