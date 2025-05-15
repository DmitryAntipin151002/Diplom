import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { eventAPI } from '../../services/eventService';
import EventForm from '../Event_Component/EventForm';
import './../../assets/Dashboard.css';

const MyGames = () => {
    const [events, setEvents] = useState([]);
    const [showCreateForm, setShowCreateForm] = useState(false);
    const userId = localStorage.getItem('userId');
    const navigate = useNavigate();

    useEffect(() => {
        const loadEvents = async () => {
            try {
                const response = await eventAPI.getEventsByOrganizer(userId);
                setEvents(response.data);
            } catch (error) {
                console.error('Ошибка загрузки событий:', error);
            }
        };
        loadEvents();
    }, [userId]);

    const handleDelete = async (eventId) => {
        if (window.confirm('Вы уверены, что хотите удалить это событие?')) {
            try {
                await eventAPI.deleteEvent(eventId);
                setEvents(events.filter(event => event.id !== eventId));
            } catch (error) {
                console.error('Ошибка удаления:', error);
            }
        }
    };

    const handleStatusChange = async (eventId, newStatus) => {
        try {
            await eventAPI.updateEventStatus(eventId, newStatus);
            setEvents(events.map(event =>
                event.id === eventId ? {...event, statusCode: newStatus} : event
            ));
        } catch (error) {
            console.error('Ошибка обновления статуса:', error);
        }
    };

    return (
        <div className="dashboard-container">
            {/* Общая структура как в Dashboard */}
            <main className="main-content">
                <header className="dashboard-header">
                    {/* Хедер аналогичный Dashboard */}
                </header>

                <section className="my-games-content">
                    <div className="section-header">
                        <h2>Мои организованные игры</h2>
                        <button
                            className="create-button"
                            onClick={() => setShowCreateForm(!showCreateForm)}
                        >
                            {showCreateForm ? 'Отменить' : 'Создать новую игру'}
                        </button>
                    </div>

                    {showCreateForm && (
                        <EventForm
                            onSuccess={(newEvent) => {
                                setEvents([...events, newEvent]);
                                setShowCreateForm(false);
                            }}
                        />
                    )}

                    <div className="events-list">
                        {events.map(event => (
                            <div key={event.id} className="event-card">
                                <div className="event-header">
                                    <h3>{event.title}</h3>
                                    <span className={`status-badge ${event.statusCode.toLowerCase()}`}>
                    {event.statusCode}
                  </span>
                                </div>

                                <div className="event-details">
                                    <p>📍 {event.location}</p>
                                    <p>⏰ {new Date(event.startTime).toLocaleString()}</p>
                                    <p>👥 Участников: {event.participantsCount || 0}</p>
                                </div>

                                <div className="event-actions">
                                    <select
                                        value={event.statusCode}
                                        onChange={(e) => handleStatusChange(event.id, e.target.value)}
                                        className="status-select"
                                    >
                                        <option value="PLANNED">Запланировано</option>
                                        <option value="ONGOING">В процессе</option>
                                        <option value="COMPLETED">Завершено</option>
                                    </select>

                                    <button
                                        className="edit-button"
                                        onClick={() => navigate(`/events/${event.id}/edit`)}
                                    >
                                        Редактировать
                                    </button>

                                    <button
                                        className="delete-button"
                                        onClick={() => handleDelete(event.id)}
                                    >
                                        Удалить
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                </section>
            </main>
        </div>
    );
};

export default MyGames;