import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../../services/AuthService";
import Alert2 from "../../../../UI/Alert";
import RoleSelector from "./RoleSelector";
import "../../styles/RegisterPage.css";

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
        setError('');
        setSuccessMessage('');

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

    return (
        <div className="register-container">
            <div className="register-glass">

                {error && <Alert2 type="error" message={error} onClose={handleCloseAlert} />}
                {successMessage && <Alert2 type="success" message={successMessage} onClose={handleCloseAlert} />}

                <div className="register-content">
                    <div className="auth-header">
                        <img
                            src="/Logo.jpg"
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

                        <RoleSelector roleId={roleId} onRoleChange={setRoleId} />

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
                        <a href="/login" className="link"> Войти здесь</a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;
