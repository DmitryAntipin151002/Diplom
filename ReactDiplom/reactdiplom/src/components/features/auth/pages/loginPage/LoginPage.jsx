import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../../services/AuthService";
import InputField from "../../../../UI/InputField";
import Alert from "../../../../UI/Alert";
import Spinner from "../../../../UI/Spinner";
import "../../styles/LoginPage.css";

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const response = await AuthService.login({ email, password });
            if (response?.data?.preAuthorizationToken && response?.data?.id) {
                localStorage.setItem('isFirstEnterToken', response.data.preAuthorizationToken);
                localStorage.setItem('userId', response.data.id);
                navigate('/verify-code', {
                    state: {
                        userId: response.data.id,
                        tempToken: response.data.preAuthorizationToken
                    }
                });
            } else {
                throw new Error("Недостаточно данных для авторизации");
            }
        } catch (err) {
            console.error("Login error:", err);
            const errorMsg = err.response?.data?.message || err.message || "Ошибка авторизации";
            setError(errorMsg);
            setTimeout(() => setError(''), 5000);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-glass">
                {error && <Alert type="error" message={error} onClose={() => setError('')} />}

                <div className="auth-content">
                    <div className="auth-header">
                        <img src="/Logo.jpg" alt="Логотип" className="logo-float" />
                        <h1 className="gradient-text">С возвращением!</h1>
                        <p className="subtitle">Войдите в свой аккаунт</p>
                    </div>

                    <form onSubmit={handleLogin} className="auth-form">
                        <InputField
                            label="Email"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />

                        <InputField
                            label="Пароль"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />

                        <div className="auth-options">
                            <label className="remember-me">
                                <input type="checkbox" />
                                <span className="checkmark"></span>
                                Запомнить меня
                            </label>
                            <a href="/recovery" className="forgot-password">Забыли пароль?</a>
                        </div>

                        <button type="submit" className="auth-button" disabled={loading}>
                            {loading ? <Spinner /> : <>Войти <span className="arrow">→</span></>}
                        </button>

                        <p className="auth-footer">
                            Нет аккаунта? <a href="/register" className="link">Зарегистрироваться</a>
                        </p>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;
