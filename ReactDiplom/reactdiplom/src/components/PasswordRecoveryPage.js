import React, { useState } from 'react';
import AuthService from '../services/AuthService';

const PasswordRecoveryPage = () => {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await AuthService.recoverPassword({ email });
            setMessage('Инструкции по восстановлению пароля отправлены на ваш email');
        } catch (error) {
            setMessage('Ошибка при восстановлении пароля');
        }
    };

    return (
        <div className="container">
            <h1>Восстановление пароля</h1>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Введите email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                {message && <p>{message}</p>}
                <button type="submit">Отправить ссылку на восстановление</button>
            </form>
        </div>
    );
};

export default PasswordRecoveryPage;
