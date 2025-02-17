import React, { createContext, useContext, useState } from 'react';
import { login as authLogin, register as authRegister } from '../services/authService';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    const login = async (credentials) => {
        const data = await authLogin(credentials);
        setUser(data);
    };

    const register = async (credentials) => {
        const data = await authRegister(credentials);
        setUser(data);
    };

    return (
        <AuthContext.Provider value={{ user, login, register }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);