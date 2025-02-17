import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/auth.css'; // Импортируем стили

const LoginPage = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        remember: false
    });

    const handleSubmit = (e) => {
        e.preventDefault();
        // Логика авторизации
    };

    return (
        <div className="wrapper">
            <div className="header">
                <h3 className="sign-in">Sign in</h3>
                <Link to="/register" className="button">
                    Register
                </Link>
            </div>

            <form onSubmit={handleSubmit}>
                <div className="input-group">
                    <label className="user" htmlFor="email">
                        <svg viewBox="0 0 32 32">
                            <use xlinkHref="#man-people-user"></use>
                        </svg>
                    </label>
                    <input
                        type="email"
                        id="email"
                        placeholder="Email"
                        value={formData.email}
                        onChange={(e) => setFormData({...formData, email: e.target.value})}
                    />
                </div>

                <div className="input-group">
                    <label className="lock" htmlFor="password">
                        <svg viewBox="0 0 32 32">
                            <use xlinkHref="#lock-locker"></use>
                        </svg>
                    </label>
                    <input
                        type="password"
                        id="password"
                        placeholder="Password"
                        value={formData.password}
                        onChange={(e) => setFormData({...formData, password: e.target.value})}
                    />
                </div>

                <div className="form-footer">
                    <div className="remember-me">
                        <div className="radio-check">
                            <input
                                type="checkbox"
                                id="remember"
                                checked={formData.remember}
                                onChange={(e) => setFormData({...formData, remember: e.target.checked})}
                            />
                            <label htmlFor="remember">Remember me</label>
                        </div>
                    </div>
                    <Link to="/forgot-password" className="forgot-label">
                        Lost your password?
                    </Link>
                </div>

                <button type="submit" className="submit-btn">
                    Sign in
                </button>
            </form>
        </div>
    );
};

export default LoginPage;