// Dashboard.jsx
import React from 'react';
import './../assets/Dashboard.css';

const Dashboard = () => {
    const games = [
        { id: 1, title: "Футбол в парке", location: "Центральный парк", date: "Сегодня 18:00", players: 12, maxPlayers: 16 },
        { id: 2, title: "Волейбол на пляже", location: "Солнечный пляж", date: "Завтра 11:00", players: 8, maxPlayers: 12 },
        { id: 3, title: "Баскетбол под открытым небом", location: "Школьная площадка", date: "20.07 17:30", players: 5, maxPlayers: 10 }
    ];

    return (
        <div className="dashboard-container">
            {/* Навигационная панель */}
            <nav className="sidebar">
                <div className="logo">Game<span>Hub</span></div>
                <ul className="nav-menu">
                    <li className="active"><span>🏠</span>Главная</li>
                    <li><span>🎯</span>Мои игры</li>
                    <li><span>🗓️</span>Календарь</li>
                    <li><span>👥</span>Сообщества</li>
                    <li><span>⚙️</span>Настройки</li>
                </ul>
            </nav>

            {/* Основной контент */}
            <main className="main-content">
                {/* Хедер */}
                <header className="dashboard-header">
                    <div className="search-bar">
                        <input type="text" placeholder="Поиск игр по локации, виду спорта..." />
                        <button>🔍</button>
                    </div>
                    <div className="user-profile">
                        <span className="notification">🔔<div className="badge">3</div></span>
                        <img src="user-avatar.jpg" alt="Профиль" />
                    </div>
                </header>

                {/* Быстрое создание игры */}
                <section className="quick-create">
                    <button className="create-button">
                        🚀 Создать игру
                        <span>+</span>
                    </button>
                </section>

                {/* Карта и список игр */}
                <div className="content-grid">
                    <section className="game-map">
                        <div className="map-placeholder">
                            🗺️ Интерактивная карта активностей
                        </div>
                    </section>

                    <section className="game-list">
                        <h2>Ближайшие игры <span>🔥 Активно формируются</span></h2>
                        {games.map(game => (
                            <div key={game.id} className="game-card">
                                <div className="game-header">
                                    <h3>{game.title}</h3>
                                    <span className="sport-icon">⚽</span>
                                </div>
                                <div className="game-details">
                                    <p>📍 {game.location}</p>
                                    <p>⏰ {game.date}</p>
                                    <div className="players-progress">
                                        <div className="progress-bar"
                                             style={{width: `${(game.players/game.maxPlayers)*100}%`}}>
                                        </div>
                                        <span>{game.players}/{game.maxPlayers} участников</span>
                                    </div>
                                </div>
                                <button className="join-button">Присоединиться →</button>
                            </div>
                        ))}
                    </section>
                </div>
            </main>

            {/* Боковая панель */}
            <aside className="right-sidebar">
                <div className="weather-widget">
                    <h3>Погода для игр</h3>
                    <div className="weather-content">
                        <span>🌤️ 24°C</span>
                        <p>Идеально для активностей на улице!</p>
                    </div>
                </div>

                <div className="recommended-games">
                    <h3>Рекомендуем попробовать</h3>
                    <div className="recommendation-card">
                        <h4>Фрисби-гольф</h4>
                        <p>Парк культуры · 2 км от вас</p>
                        <button>Подробнее</button>
                    </div>
                </div>
            </aside>
        </div>
    );
};

export default Dashboard;