import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../assets/LoginPage.css';

const ErrorPage = () => {
    const navigate = useNavigate();
    const { state } = useLocation();

    return (
        <div className="auth-container">
            <div className="auth-glass error-glass">
                <div className="auth-content">
                    <h1 className="gradient-text">Ошибка!</h1>
                    <p className="subtitle">{state?.message || 'Произошла непредвиденная ошибка'}</p>

                    <button
                        className="auth-button"
                        onClick={() => navigate('/login')}
                    >
                        Вернуться на главную
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ErrorPage;