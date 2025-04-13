import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';
import PasswordRecoveryPage from './components/PasswordRecoveryPage';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/password-recovery" element={<PasswordRecoveryPage />} />
            </Routes>
        </Router>
    );
}

export default App; // 🔥 ОБЯЗАТЕЛЬНО!
