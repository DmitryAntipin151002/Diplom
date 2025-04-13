import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';  // заменили useHistory на useNavigate
import AuthService from '../services/AuthService';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState(''); // Состояние для успешного сообщения
    const navigate = useNavigate();  // используем useNavigate

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await AuthService.login({ email, password });
            if (response.status === 200) {
                setSuccessMessage('Авторизация успешна!');  // Устанавливаем сообщение об успехе
                setTimeout(() => {
                    navigate('/dashboard'); // Редирект на главную страницу через 2 секунды
                }, 2000);  // Задержка перед редиректом
            }
        } catch (error) {
            setError('Неверный email или пароль');
        }
    };

    return (
        <div className="container">
            <h1>Авторизация</h1>
            {successMessage && <p className="success">{successMessage}</p>} {/* Отображаем сообщение об успехе */}
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Пароль"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                {error && <p className="error">{error}</p>}
                <button type="submit">Войти</button>
            </form>
            <p>
                Нет аккаунта? <a href="/register">Зарегистрироваться</a>
            </p>
            <p>
                Забыли пароль? <a href="/recovery">Восстановить пароль</a>
            </p>
        </div>
    );
};

export default LoginPage;
