import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './components/AuthS_Component/LoginPage';
import RegisterPage from './components/AuthS_Component/RegisterPage';
import VerifyCodePage from "./components/AuthS_Component/VerifyCodePage";
import ErrorPage from "./components/AuthS_Component/ErrorPage";
import RecoveryRequestPage from "./components/AuthS_Component/RecoveryRequestPage";
import RecoveryPasswordPage from "./components/AuthS_Component/RecoveryPasswordPage";
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
