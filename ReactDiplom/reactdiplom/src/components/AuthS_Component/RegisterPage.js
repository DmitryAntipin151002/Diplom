import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../../services/AuthService";
import "../../assets/AuthS/RegisterPage.css";

const RegisterPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [roleId, setRoleId] = useState(2);
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            const response = await AuthService.register({ email, password, roleId });
            if (response.status === 200) {
                setSuccessMessage("🎉 Регистрация успешна! Перенаправляем...");
                setTimeout(() => navigate('/login'), 2000);
            }
        } catch (err) {
            setError(err.response?.data?.message || "⚠️ Ошибка при регистрации");
        } finally {
            setLoading(false);
        }
    };

    const handleCloseAlert = () => {
        setError('');
        setSuccessMessage('');
    };

    const handleRoleChange = (selectedRoleId) => {
        setRoleId(selectedRoleId);
    };

    return (
        <div className="register-container">
            <div className="register-glass">
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

                <div className="register-content">
                    <div className="auth-header">
                        <img
                            src="https://storage.googleapis.com/tagjs-prod.appspot.com/v1/RC5xlerM5u/mej84c5v_expires_30_days.png"
                            alt="Логотип"
                            className="logo-spin"
                        />
                        <h1 className="gradient-text">Добро пожаловать!</h1>
                        <p className="subtitle">Создайте новый аккаунт</p>
                    </div>

                    <form onSubmit={handleRegister} className="auth-form">
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
                                minLength="6"
                            />
                            <label className="floating-label">Пароль</label>
                        </div>

                        <div className="role-selector">
                            <div className="role-options">
                                <label className={`role-card ${roleId === 1 ? 'active' : ''}`}>
                                    <input
                                        type="radio"
                                        name="role"
                                        value={1}
                                        checked={roleId === 1}
                                        onChange={() => handleRoleChange(1)}
                                    />
                                    <div className="role-content">
                                        <span className="role-icon">👑</span>
                                        <span className="role-title">Администратор</span>
                                        <span className="role-desc">Полный доступ к системе</span>
                                    </div>
                                </label>

                                <label className={`role-card ${roleId === 2 ? 'active' : ''}`}>
                                    <input
                                        type="radio"
                                        name="role"
                                        value={2}
                                        checked={roleId === 2}
                                        onChange={() => handleRoleChange(2)}
                                    />
                                    <div className="role-content">
                                        <span className="role-icon">👤</span>
                                        <span className="role-title">Пользователь</span>
                                        <span className="role-desc">Базовые возможности</span>
                                    </div>
                                </label>
                            </div>
                        </div>

                        <button
                            type="submit"
                            className="submit-btn"
                            disabled={loading}
                        >
                            {loading ? (
                                <div className="spinner"></div>
                            ) : (
                                <>
                                    <span>Создать аккаунт</span>
                                    <span className="arrow">→</span>
                                </>
                            )}
                        </button>
                    </form>

                    <p className="auth-footer">
                        Уже есть аккаунт?
                        <a href="/reactdiplom/public" className="link">Войти здесь</a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;