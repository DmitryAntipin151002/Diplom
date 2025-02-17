import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import '../../styles/auth.css'; // Подключаем стили

const LoginForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await login({ email, password });
            navigate('/profile'); // Перенаправление после успешного входа
        } catch (err) {
            setError('Invalid email or password');
        }
    };

    return (
        <div className="wrapper">
            <div className="header">
                <h3 className="sign-in">Sign in</h3>
                <div className="button" onClick={() => navigate('/register')}>
                    Register
                </div>
            </div>
            <div className="clear"></div>
            <form onSubmit={handleSubmit}>
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <div>
                    <label className="user" htmlFor="email">
                        <svg viewBox="0 0 32 32">
                            <use xlinkHref="#man-people-user"></use>
                        </svg>
                    </label>
                    <input
                        className="user-input"
                        type="email"
                        name="email"
                        id="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label className="lock" htmlFor="password">
                        <svg viewBox="0 0 32 32">
                            <use xlinkHref="#lock-locker"></use>
                        </svg>
                    </label>
                    <input
                        type="password"
                        name="password"
                        id="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <input type="submit" value="Sign in" />
                </div>
                <div className="radio-check">
                    <input type="radio" className="radio-no" id="no" name="remember" value="no" defaultChecked />
                    <label htmlFor="no"><i className="fa fa-times"></i></label>
                    <input type="radio" className="radio-yes" id="yes" name="remember" value="yes" />
                    <label htmlFor="yes"><i className="fa fa-check"></i></label>
                    <span className="switch-selection"></span>
                </div>
                <span className="check-label">Remember me</span>
                <span className="forgot-label">Lost your password?</span>
                <div className="clear"></div>
            </form>
        </div>
    );
};

export default LoginForm;
