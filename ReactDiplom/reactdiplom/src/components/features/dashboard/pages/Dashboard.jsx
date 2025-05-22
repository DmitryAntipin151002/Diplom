import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import '../styles/Dashboard.css';
import { eventAPI } from "../../event/services/eventService";

const Dashboard = () => {
    const [showProfileMenu, setShowProfileMenu] = useState(false);
    const navigate = useNavigate();
    const profileRef = useRef(null);
    const userId = localStorage.getItem('userId');
    const [allEvents, setAllEvents] = useState([]);

    // Загрузка данных
    useEffect(() => {
        const loadData = async () => {
            try {
                const [eventsRes] = await Promise.all([
                    eventAPI.getAllEvents()
                ]);
                setAllEvents(eventsRes.data);
            } catch (error) {
                console.error('Ошибка загрузки:', error);
            }
        };
        loadData();
    }, []);

    // Обработчики
    const handleLogout = () => {
        localStorage.removeItem('isFirstEnterToken');
        navigate('/login');
    };

    const getSportIcon = (type) => ({
        FOOTBALL: '⚽',
        BASKETBALL: '🏀',
        VOLLEYBALL: '🏐',
        TENNIS: '🎾'
    }[type] || '🏅');

    const getEventStatus = (startTime) => {
        const now = new Date();
        return new Date(startTime) > now ? 'Запланировано' : 'Завершено';
    };

    return (
        <div className="dashboard-container">
            {/* Сайдбар */}
            <nav className="sidebar">
                <div className="logo">
                    <span className="gradient-text">Game</span>
                    <span className="accent-text">Hub</span>
                </div>

                <ul className="nav-menu">
                    {[
                        { path: '/dashboard', icon: '🏠', text: 'Главная' },
                        { path: '/my-events', icon: '📅', text: 'Мероприятия' },
                        { path: '/communities', icon: '👥', text: 'Сообщества' },
                        { path: '/chats', icon: '💬', text: 'Мессенджер' },
                        { path: '/settings', icon: '⚙️', text: 'Настройки' }
                    ].map((item, index) => (
                        <motion.li
                            key={index}
                            whileHover={{ scale: 1.05 }}
                            onClick={() => navigate(item.path)}
                        >
                            <span className="nav-icon">{item.icon}</span>
                            <span className="nav-text">{item.text}</span>
                        </motion.li>
                    ))}
                </ul>
            </nav>

            {/* Основной контент */}
            <main className="main-content">
                <header className="dashboard-header">
                    <div className="search-container">
                        <input
                            type="text"
                            placeholder="Поиск мероприятий..."
                            className="modern-input"
                        />
                        <button className="search-btn">
                            <span className="icon">🔍</span>
                        </button>
                    </div>

                    <div className="profile-section">


                        <motion.div
                            className="avatar-container"
                            onClick={() => setShowProfileMenu(!showProfileMenu)}
                            ref={profileRef}
                            whileHover={{scale: 1.05}}
                            whileTap={{scale: 0.95}}
                        >

                            <div className="avatar-border">
                                <img
                                    src="/Avatar.jpg"
                                    className="user-avatar"
                                />
                            </div>

                            <AnimatePresence>
                                {showProfileMenu && (
                                    <motion.div
                                        className="profile-menu"
                                        initial={{opacity: 0, y: 20}}
                                        animate={{opacity: 1, y: 0}}
                                        exit={{opacity: 0, y: 20}}
                                    >
                                        <div className="menu-item" onClick={() => navigate(`/profile/${userId}`)}>
                                            <span className="menu-icon">👤</span>
                                            Профиль
                                            <div className="hover-line"></div>
                                        </div>
                                        <div className="menu-divider"></div>
                                        <div className="menu-item" onClick={() => navigate('/settings')}>
                                            <span className="menu-icon">⚙️</span>
                                            Настройки
                                            <div className="hover-line"></div>
                                        </div>
                                        <div className="menu-divider"></div>
                                        <div className="menu-item logout" onClick={handleLogout}>
                                            <span className="menu-icon">🚪</span>
                                            Выйти
                                            <div className="hover-line"></div>
                                        </div>
                                    </motion.div>
                                )}
                            </AnimatePresence>
                        </motion.div>
                    </div>
                </header>

                {/* Секция создания события */}
                <section className="cta-section">
                    <motion.button
                        className="create-event-btn"
                        whileHover={{
                            scale: 1.05,
                            boxShadow: "0 8px 20px rgba(16, 185, 129, 0.3)"
                        }}
                        whileTap={{scale: 0.95}}
                        onClick={() => navigate('/create-event')}
                    >
                        <span className="plus-icon">+</span>
                        Создать новое мероприятие
                        <div className="hover-shine"></div>
                    </motion.button>
                </section>

                {/* Список мероприятий */}
                <section className="events-section">
                    <h2 className="section-title">
                        <span className="title-text">Мои мероприятия</span>
                        <div className="title-line"></div>
                    </h2>

                    <div className="events-grid">
                        {allEvents.map(event => (
                            <motion.div
                                key={event.id}
                                className="event-card"
                                whileHover={{y: -5}}
                            >
                                <div className="card-header">
                                    <div className="status-indicator">
                                        <span
                                            className={`status-dot ${getEventStatus(event.startTime).toLowerCase()}`}></span>
                                        <span className="status-text">{getEventStatus(event.startTime)}</span>
                                    </div>
                                    <span className="sport-icon">{getSportIcon(event.sportType)}</span>
                                </div>

                                <div className="card-content">
                                    <h3 className="event-title">{event.title}</h3>

                                    <div className="event-meta">
                                        <div className="meta-item">
                                            <span className="icon">📅</span>
                                            {new Date(event.startTime).toLocaleDateString('ru-RU', {
                                                day: 'numeric',
                                                month: 'short',
                                                hour: '2-digit',
                                                minute: '2-digit'
                                            })}
                                        </div>
                                        <div className="meta-item">
                                            <span className="icon">📍</span>
                                            {event.location}
                                        </div>
                                    </div>

                                    <div className="progress-section">
                                        <div className="progress-bar">
                                            <div
                                                className="progress-fill"
                                                style={{width: `${(event.participantsCount / event.maxParticipants) * 100}%`}}
                                            ></div>
                                        </div>
                                        <div className="participants-info">
                                            <span>{event.participantsCount}/{event.maxParticipants}</span>
                                            <button
                                                className="action-btn"
                                                onClick={() => navigate(`/events/${event.id}`)}
                                            >
                                                {getEventStatus(event.startTime) === 'Запланировано' ? 'Редактировать' : 'Результаты'}
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </motion.div>
                        ))}
                    </div>
                </section>
            </main>

            {/* Правая панель */}
            <aside className="right-panel">
                <div className="weather-card">
                    <div className="weather-header">
                        <span className="icon">🌤️</span>
                        <div className="weather-info">
                            <div className="temperature">+24°C</div>
                            <div className="description">Идеально для активности!</div>
                        </div>
                    </div>
                    <div className="weather-details">
                        <div className="detail-item">
                            <span>💨</span>
                            <div>Ветер 5 м/с</div>
                        </div>
                        <div className="detail-item">
                            <span>💧</span>
                            <div>Влажность 65%</div>
                        </div>
                    </div>
                </div>

                <div className="online-friends">
                    <h3 className="friends-title">Друзья онлайн <span className="count">5</span></h3>

                    <div className="friends-list">
                        {['Алексей', 'Мария', 'Дмитрий', 'Екатерина', 'Иван'].map((name, index) => (
                            <div className="friend-item" key={index}>
                                <div className="friend-avatar">
                                    {name[0]}
                                </div>
                                <div className="friend-name">{name}</div>
                                <button className="message-btn">
                                    <span className="icon">💬</span>
                                </button>
                            </div>
                        ))}
                    </div>
                </div>
            </aside>
        </div>
    );
};

export default Dashboard;