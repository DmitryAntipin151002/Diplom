import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const ProtectedRoute = ({ children }) => {
    const { user } = useAuth(); // Получаем текущего пользователя из контекста

    // Если пользователь не авторизован, перенаправляем на страницу входа
    if (!user) {
        return <Navigate to="/login" replace />;
    }

    // Если пользователь авторизован, отображаем дочерние элементы
    return children;
};

export default ProtectedRoute;