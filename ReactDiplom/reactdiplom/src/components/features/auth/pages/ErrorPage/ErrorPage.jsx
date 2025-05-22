import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ErrorHeader from './ErrorHeader';
import ErrorMessage from './ErrorMessage';
import '../../../auth/styles/LoginPage.css'; // При желании — переименовать

const ErrorPage = () => {
    const navigate = useNavigate();
    const { state } = useLocation();

    return (
        <div className="auth-container">
            <div className="auth-glass error-glass">
                <div className="auth-content">
                    <ErrorHeader />
                    <ErrorMessage message={state?.message} />
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

