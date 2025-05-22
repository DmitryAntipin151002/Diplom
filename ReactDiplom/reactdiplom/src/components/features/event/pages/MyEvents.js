// components/MyEvents.jsx
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { eventAPI } from '../services/eventService';
import EventForm from './EventForm';
import '../styles/MyEvents.css';

const MyEvents = () => {
    const [events, setEvents] = useState([]);
    const [showCreateForm, setShowCreateForm] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const userId = localStorage.getItem('userId');
    const navigate = useNavigate();

    useEffect(() => {
        const loadEvents = async () => {
            try {
                setLoading(true);
                const response = await eventAPI.getMyEvents(userId);
                setEvents(response.data);
            } catch (error) {
                console.error('Ошибка загрузки событий:', error);
                setError('Не удалось загрузить ваши мероприятия');
            } finally {
                setLoading(false);
            }
        };

        loadEvents();
    }, [userId]);

    const handleCreateSuccess = (newEvent) => {
        setEvents([newEvent, ...events]);
        setShowCreateForm(false);
        navigate(`/events/${newEvent.id}`, {
            state: { message: 'Мероприятие успешно создано' }
        });
    };

    const getSportIcon = (sportType) => {
        switch (sportType) {
            case 'FOOTBALL': return '⚽';
            case 'BASKETBALL': return '🏀';
            case 'VOLLEYBALL': return '🏐';
            case 'TENNIS': return '🎾';
            default: return '🏅';
        }
    };

    const getStatusClass = (statusCode) => {
        switch (statusCode) {
            case 'PLANNED': return 'status-planned';
            case 'ONGOING': return 'status-ongoing';
            case 'COMPLETED': return 'status-completed';
            default: return 'status-default';
        }
    };

    const getStatusText = (statusCode) => {
        switch (statusCode) {
            case 'PLANNED': return 'Запланировано';
            case 'ONGOING': return 'В процессе';
            case 'COMPLETED': return 'Завершено';
            default: return statusCode;
        }
    };

    if (loading) {
        return (
            <div className="loading-page">
                <div className="spinner"></div>
                <p>Загрузка мероприятий...</p>
            </div>
        );
    }

    if (error && events.length === 0) {
        return (
            <div className="error-page">
                <h2>Ошибка!</h2>
                <p>{error}</p>
                <button
                    onClick={() => window.location.reload()}
                    className="retry-button"
                >
                    Попробовать снова
                </button>
            </div>
        );
    }

    return (
        <div className="my-events-container">
            <div className="content-wrapper">
                <header className="header">
                    <h1>Мои мероприятия</h1>
                    <p className="subtitle">Здесь вы можете управлять своими спортивными событиями</p>
                </header>

                <div className="actions">
                    <button
                        className="create-button"
                        onClick={() => setShowCreateForm(!showCreateForm)}
                    >
                        {showCreateForm ? (
                            <>
                                <span>✖</span> Отменить создание
                            </>
                        ) : (
                            <>
                                <span>➕</span> Создать мероприятие
                            </>
                        )}
                    </button>
                </div>

                {showCreateForm && (
                    <div className="form-container">
                        <EventForm
                            onSuccess={handleCreateSuccess}
                            onCancel={() => setShowCreateForm(false)}
                        />
                    </div>
                )}

                <div className="events-grid">
                    {events.length === 0 ? (
                        <div className="no-events">
                            <p>У вас пока нет мероприятий</p>
                            <button
                                className="create-button"
                                onClick={() => setShowCreateForm(true)}
                            >
                                <span>➕</span> Создать первое мероприятие
                            </button>
                        </div>
                    ) : (
                        events.map(event => (
                            <div
                                key={event.id}
                                className="event-card"
                                onClick={() => navigate(`/events/${event.id}`)}
                            >
                                <div className="card-header">
                                    <span className="sport-icon">{getSportIcon(event.sportType)}</span>
                                    <h3>{event.title}</h3>
                                </div>
                                <div className="card-details">
                                    <p className="location">
                                        <span>📍</span>
                                        {event.location || 'Место не указано'}
                                    </p>
                                    <p className="time">
                                        <span>⏰</span>
                                        {new Date(event.startTime).toLocaleString('ru-RU', {
                                            day: 'numeric',
                                            month: 'numeric',
                                            year: 'numeric',
                                            hour: '2-digit',
                                            minute: '2-digit'
                                        })}
                                    </p>
                                </div>
                                <p className={`status ${getStatusClass(event.statusCode)}`}>
                                    {getStatusText(event.statusCode)}
                                </p>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default MyEvents;