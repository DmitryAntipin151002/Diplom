// App.js
import React from 'react';
import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom';
import LoginPage from './components/features/auth/pages/loginPage/LoginPage';
import RegisterPage from './components/features/auth/pages/registerPage/RegisterPage';
import VerifyCodePage from "./components/features/auth/pages/verifyCode/VerifyCodePage";
import ErrorPage from "./components/features/auth/pages/ErrorPage/ErrorPage";
import RecoveryRequestPage from "./components/features/auth/pages/RecoveryPassword/RecoveryRequestPage";
import RecoveryPasswordPage from "./components/features/auth/pages/RecoveryPassword/RecoveryPasswordPage";

import ChatPage from "./components/features/chat/pages/ChatPage"


import MyEvents from "./components/features/event/pages/MyEvents";
import EventPage from "./components/features/event/pages/EventPage";
import EventForm from "./components/features/event/pages/EventForm";
import Dashboard from "./components/features/dashboard/pages/Dashboard";

import ProfilePage from "./components/features/profile/pages/profilePage/ProfilePage";
import ProtectedRoute from "./components/ProtectedRoute";
import FriendsPage from "./components/features/profile/pages/profilePage/FriendsPage";





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
                    {/* Добавляем новые маршруты для чата */}
                    <Route path="/chats" element={<ChatPage />} />
                    <Route path="/chats/:chatId" element={<ChatPage />} />
                    <Route path="/" element={<Dashboard />} />
                    <Route path="/events/create" element={<EventForm />} />
                    <Route path="/my-events" element={<MyEvents />} />
                    <Route path="/events/:eventId" element={<EventPage />} />


                </Route>

                <Route path="*" element={<Navigate to="/login" replace />} />
            </Routes>
        </Router>
    );
}

export default App;