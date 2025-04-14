import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './components/LoginPage';
import RegisterPage from './components/RegisterPage';
import VerifyCodePage from "./components/VerifyCodePage";
import ErrorPage from "./components/ErrorPage";
import RecoveryRequestPage from "./components/RecoveryRequestPage";
import RecoveryPasswordPage from "./components/RecoveryPasswordPage";
import Dashboard from "./components/Dashboard";


function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/recovery" element={<RecoveryRequestPage />} />
                <Route path="/recovery/password" element={<RecoveryPasswordPage />} />
                <Route path="/verify-code" element={<VerifyCodePage />} />
                <Route path="/error" element={<ErrorPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/password-recovery" element={<RecoveryRequestPage />} />
                <Route path="/dashboard" element={<Dashboard/>} />
            </Routes>
        </Router>
    );
}

export default App; // 🔥 ОБЯЗАТЕЛЬНО!
