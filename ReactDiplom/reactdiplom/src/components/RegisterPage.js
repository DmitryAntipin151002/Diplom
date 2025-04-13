import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../services/AuthService';

const RegisterPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [roleId, setRoleId] = useState(2); // по умолчанию User
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await AuthService.register({ email, password, roleId });
            if (response.status === 200) {
                navigate('/login');
            }
        } catch (error) {
            setError('Ошибка при регистрации');
        }
    };

    return (
        <div className="container">
            <h1>Регистрация</h1>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Пароль"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <select value={roleId} onChange={(e) => setRoleId(Number(e.target.value))}>
                    <option value={1}>Admin</option>
                    <option value={2}>User</option>
                </select>
                {error && <p className="error">{error}</p>}
                <button type="submit">Зарегистрироваться</button>
            </form>
            <p>
                Уже есть аккаунт? <a href="/login">Войти</a>
            </p>
        </div>
    );
};

export default RegisterPage;
