import React, {useEffect, useRef, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import './../assets/Dashboard.css';

const Dashboard = () => {
    const [showProfileMenu, setShowProfileMenu] = useState(false);
    const navigate = useNavigate();
    const profileRef = useRef(null);
    const userId = localStorage.getItem('userId');

    const games = [
        { id: 1, title: "Футбол в парке", location: "Центральный парк", date: "Сегодня 18:00", players: 12, maxPlayers: 16, sport: '⚽' },
        { id: 2, title: "Волейбол на пляже", location: "Солнечный пляж", date: "Завтра 11:00", players: 8, maxPlayers: 12, sport: '🏐' },
        { id: 3, title: "Баскетбол под открытым небом", location: "Школьная площадка", date: "20.07 17:30", players: 5, maxPlayers: 10, sport: '🏀' }
    ];

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (profileRef.current && !profileRef.current.contains(event.target)) {
                setShowProfileMenu(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('isFirstEnterToken');
        navigate('/login');
    };

    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <div className="dashboard-container">
            {/* Навигационная панель */}
            <nav className="sidebar">
                <div className="logo">Game<span>Hub</span></div>
                <ul className="nav-menu">
                    <li className="active" onClick={() => handleNavigation('/dashboard')}>
                        <span className="nav-icon">🏠</span>
                        <span className="nav-text">Главная</span>
                    </li>
                    <li onClick={() => handleNavigation('/my-games')}>
                        <span className="nav-icon">🎯</span>
                        <span className="nav-text">Мои игры</span>
                    </li>
                    <li onClick={() => handleNavigation('/calendar')}>
                        <span className="nav-icon">🗓️</span>
                        <span className="nav-text">Календарь</span>
                    </li>
                    <li onClick={() => handleNavigation('/communities')}>
                        <span className="nav-icon">👥</span>
                        <span className="nav-text">Сообщества</span>
                    </li>
                    <li onClick={() => handleNavigation('/messenger')}>  {/* Добавлен переход к мессенджеру */}
                        <span className="nav-icon">💬</span>
                        <span className="nav-text">Мессенджер</span>
                    </li>
                    <li onClick={() => handleNavigation('/settings')}>
                        <span className="nav-icon">⚙️</span>
                        <span className="nav-text">Настройки</span>
                    </li>
                </ul>
            </nav>

            {/* Основной контент */}
            <main className="main-content">
                {/* Хедер */}
                <header className="dashboard-header">
                    <div className="search-bar">
                        <input
                            type="text"
                            placeholder="Поиск игр по локации, виду спорта..."
                            className="search-input"
                        />
                        <button className="search-button">
                            <span className="search-icon">🔍</span>
                            <span className="search-text">Найти</span>
                        </button>
                    </div>
                    <div className="user-profile-container">
                        <div
                            className="notification-bell messenger-icon"
                            onClick={() => handleNavigation('/messenger')}  // Добавлен переход к мессенджеру
                        >
                            <span className="notification-icon">💬</span>
                            <div className="badge">3</div>
                        </div>
                        <div className="notification-bell">
                            <span className="notification-icon">🔔</span>
                            <div className="badge">3</div>
                        </div>
                        <div
                            className="profile-avatar-container"
                            ref={profileRef}
                            onClick={() => setShowProfileMenu(!showProfileMenu)}
                        >
                            <img
                                src="user-avatar.jpg"
                                alt="Профиль"
                                className="profile-avatar"
                            />
                            {showProfileMenu && (
                                <div className="profile-dropdown">
                                    <div
                                        className="dropdown-item"
                                        onClick={() => handleNavigation(`/profile/${userId}`)}
                                    >
                                        <span className="dropdown-icon">👤</span>
                                        <span>Мой профиль</span>
                                    </div>
                                    <div className="dropdown-item" onClick={() => handleNavigation('/settings')}>
                                        <span className="dropdown-icon">⚙️</span>
                                        <span>Настройки</span>
                                    </div>
                                    <div
                                        className="dropdown-item logout"
                                        onClick={handleLogout}
                                    >
                                        <span className="dropdown-icon">🚪</span>
                                        <span>Выйти</span>
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </header>

                {/* Быстрое создание игры */}
                <section className="quick-create">
                    <button className="create-button">
                        <span className="create-icon">🚀</span>
                        <span className="create-text">Создать игру</span>
                        <span className="plus-icon">+</span>
                    </button>
                </section>

                {/* Карта и список игр */}
                <div className="content-grid">
                    <section className="game-map">
                        <div className="map-placeholder">
                            <div className="map-overlay">
                                <h3>Карта активностей</h3>
                                <p>Найдите игры рядом с вами</p>
                            </div>
                        </div>
                        <div className="map-filters">
                            <button className="filter-btn active">Все</button>
                            <button className="filter-btn">Футбол ⚽</button>
                            <button className="filter-btn">Волейбол 🏐</button>
                            <button className="filter-btn">Баскетбол 🏀</button>
                        </div>
                    </section>

                    <section className="game-list">
                        <div className="section-header">
                            <h2>Ближайшие игры</h2>
                            <span className="active-tag">🔥 Активно формируются</span>
                        </div>
                        <div className="games-container">
                            {games.map(game => (
                                <div key={game.id} className="game-card">
                                    <div className="game-header">
                                        <h3>{game.title}</h3>
                                        <span className="sport-icon">{game.sport}</span>
                                    </div>
                                    <div className="game-details">
                                        <div className="detail-item">
                                            <span className="detail-icon">📍</span>
                                            <span>{game.location}</span>
                                        </div>
                                        <div className="detail-item">
                                            <span className="detail-icon">⏰</span>
                                            <span>{game.date}</span>
                                        </div>
                                        <div className="players-progress">
                                            <div className="progress-container">
                                                <div
                                                    className="progress-bar"
                                                    style={{width: `${(game.players/game.maxPlayers)*100}%`}}
                                                ></div>
                                            </div>
                                            <span className="players-count">
                                                {game.players}/{game.maxPlayers} участников
                                            </span>
                                        </div>
                                    </div>
                                    <button className="join-button">
                                        <span>Присоединиться</span>
                                        <span className="arrow-icon">→</span>
                                    </button>
                                </div>
                            ))}
                        </div>
                    </section>
                </div>
            </main>

            {/* Боковая панель */}
            <aside className="right-sidebar">
                <div className="sidebar-widget weather-widget">
                    <h3 className="widget-title">Погода для игр</h3>
                    <div className="weather-content">
                        <div className="weather-icon">🌤️</div>
                        <div className="weather-info">
                            <span className="temperature">24°C</span>
                            <p className="weather-desc">Идеально для активностей на улице!</p>
                        </div>
                    </div>
                </div>

                <div className="sidebar-widget recommended-games">
                    <h3 className="widget-title">Рекомендуем попробовать</h3>
                    <div className="recommendation-card">
                        <div className="recommendation-icon">🥏</div>
                        <div className="recommendation-info">
                            <h4>Фрисби-гольф</h4>
                            <p className="location">Парк культуры · 2 км от вас</p>
                            <button className="details-btn">Подробнее</button>
                        </div>
                    </div>
                </div>

                <div className="sidebar-widget friends-widget">
                    <h3 className="widget-title">Друзья онлайн (5)</h3>
                    <div className="friends-list">
                        {['Алексей', 'Мария', 'Дмитрий', 'Екатерина', 'Иван'].map((name, index) => (
                            <div key={index} className="friend-item">
                                <div className="friend-avatar">
                                    {name.charAt(0)}
                                </div>
                                <span className="friend-name">{name}</span>
                                <button
                                    className="invite-btn"
                                    onClick={() => handleNavigation('/messenger')}  // Добавлен переход к мессенджеру
                                >
                                    Написать
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