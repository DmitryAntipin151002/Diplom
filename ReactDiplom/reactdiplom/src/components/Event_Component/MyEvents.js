import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { eventAPI } from '../../services/eventService';
import EventForm from '../Event_Component/EventForm';
import './../../assets/MyEvents.css';

const MyEvents = () => {
    const [events, setEvents] = useState([]);
    const [showCreateForm, setShowCreateForm] = useState(false);
    const userId = localStorage.getItem('userId');
    const navigate = useNavigate();

    useEffect(() => {
        const loadEvents = async () => {
            try {
                const response = await eventAPI.getMyEvents(userId);
                setEvents(response.data);
            } catch (error) {
                console.error('Ошибка загрузки событий:', error);
            }
        };
        loadEvents();
    }, [userId]);

    const handleCreateSuccess = (newEvent) => {
        setEvents([...events, newEvent]);
        setShowCreateForm(false);
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

    return (
        <div className="my-events-container">
            <div className="content-wrapper">
                <header className="header">
                    <h1>Мои мероприятия</h1>
                </header>

                <div className="button-group">
                    <button
                        className="create-button"
                        onClick={() => setShowCreateForm(!showCreateForm)}
                    >
                        {showCreateForm ? 'Отменить создание' : 'Создать мероприятие'}
                    </button>
                    <button
                        className="home-button"
                        onClick={() => navigate('/')}
                    >
                        Перейти на главную
                    </button>
                </div>

                {showCreateForm && (
                    <div className="form-container">
                        <EventForm onSuccess={handleCreateSuccess} />
                    </div>
                )}

                <div className="events-grid">
                    {events.length === 0 ? (
                        <p className="no-events">
                            У вас пока нет мероприятий. Создайте первое!
                        </p>
                    ) : (
                        events.map(event => (
                            <div
                                key={event.id}
                                className="event-card"
                                onClick={() => navigate(`/events/${event.id}`)}
                            >
                                <div className="event-header">
                                    <span className="sport-icon">{getSportIcon(event.sportType)}</span>
                                    <h3>{event.title}</h3>
                                </div>
                                <p className="event-detail">
                                    <span className="icon">📍</span>
                                    {event.location}
                                </p>
                                <p className="event-detail">
                                    <span className="icon">⏰</span>
                                    {new Date(event.startTime).toLocaleString('ru-RU')}
                                </p>
                                <p className={`event-status ${getStatusClass(event.statusCode)}`}>
                                    {event.statusCode}
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