// App.js
import React from 'react';
import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom';
import LoginPage from './components/AuthS_Component/LoginPage';
import RegisterPage from './components/AuthS_Component/RegisterPage';
import VerifyCodePage from "./components/AuthS_Component/VerifyCodePage";
import ErrorPage from "./components/AuthS_Component/ErrorPage";
import RecoveryRequestPage from "./components/AuthS_Component/RecoveryRequestPage";
import RecoveryPasswordPage from "./components/AuthS_Component/RecoveryPasswordPage";
import Dashboard from "./components/Dashboard";
import ProfilePage from "./components/Profile_Componemt/ProfilePage";
import ProtectedRoute from "./components/ProtectedRoute";
import FriendsPage from "./components/Profile_Componemt/FriendsPage";


// Заглушки для страниц

const SettingsPage = () => <div className="page-container">Settings Page</div>;
const MyGamesPage = () => <div className="page-container">My Games Page</div>;
const CalendarPage = () => <div className="page-container">Calendar Page</div>;
const CommunitiesPage = () => <div className="page-container">Communities Page</div>;

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/verify-code" element={<VerifyCodePage />} />
                <Route path="/error" element={<ErrorPage />} />
                <Route path="/recovery" element={<RecoveryRequestPage />} />
                <Route path="/recovery/password" element={<RecoveryPasswordPage />} />

                <Route element={<ProtectedRoute />}>
                    <Route path="/dashboard" element={<Dashboard />} />
                    <Route path="/profile/:userId" element={<ProfilePage />} />
                    <Route path="/settings" element={<SettingsPage />} />
                    <Route path="/my-games" element={<MyGamesPage />} />
                    <Route path="/calendar" element={<CalendarPage />} />
                    <Route path="/communities" element={<CommunitiesPage />} />
                    <Route path="/friends/:userId" element={<FriendsPage />} />
                </Route>

                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        </Router>
    );
}

export default App;