import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { eventAPI } from '../services/eventService';
import { searchProfiles } from '../../profile/services/ProfileService';
import EventForm from './EventForm';
import '../styles/EventPage.css';

const ParticipantList = ({ eventId, currentUserId, onParticipantRemoved }) => {
    const [participants, setParticipants] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const loadParticipants = async () => {
            try {
                setLoading(true);
                const response = await eventAPI.getParticipants(eventId);
                setParticipants(response.data);
            } catch (error) {
                console.error('Ошибка загрузки участников:', error);
                setError('Не удалось загрузить список участников');
            } finally {
                setLoading(false);
            }
        };

        loadParticipants();
    }, [eventId, onParticipantRemoved]);

    const handleRemoveParticipant = async (participantId) => {
        if (window.confirm('Удалить участника?')) {
            try {
                await eventAPI.removeParticipant(eventId, participantId, currentUserId);
                setParticipants(participants.filter(p => p.id !== participantId));
                if (onParticipantRemoved) {
                    onParticipantRemoved(participantId);
                }
            } catch (error) {
                console.error('Ошибка удаления участника:', error);
                setError('Не удалось удалить участника');
            }
        }
    };

    if (loading) return <div className="loading">Загрузка участников...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="participants-list-container">
            <h3 className="participants-title">Участники <span className="participants-count">{participants.length}</span></h3>

            {participants.length === 0 ? (
                <p className="no-participants">Пока никто не присоединился</p>
            ) : (
                <ul className="participants-grid">
                    {participants.map(participant => (
                        <li key={participant.id} className="participant-card">
                            <div className="participant-avatar-container">
                                <img
                                    src={participant.userAvatar || '/default-avatar-dark.png'}
                                    alt="Аватар"
                                    className="participant-avatar"
                                />
                            </div>
                            <div className="participant-info">
                                <span className="participant-name">{participant.userName}</span>
                                <button
                                    className="remove-participant-btn"
                                    onClick={() => handleRemoveParticipant(participant.id)}
                                >
                                    Удалить
                                </button>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

const EventPage = () => {
    const { eventId } = useParams();
    const navigate = useNavigate();
    const userId = localStorage.getItem('userId');
    const [event, setEvent] = useState(null);
    const [participants, setParticipants] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [showEditForm, setShowEditForm] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const loadEventData = async () => {
            try {
                setLoading(true);
                const [eventResponse, participantsResponse] = await Promise.all([
                    eventAPI.getEventDetails(eventId),
                    eventAPI.getParticipants(eventId)
                ]);

                setEvent(eventResponse.data);
                setParticipants(participantsResponse.data);
            } catch (error) {
                console.error('Ошибка загрузки:', error);
                setError('Не удалось загрузить данные мероприятия');
            } finally {
                setLoading(false);
            }
        };

        loadEventData();
    }, [eventId]);

    const handleDelete = async () => {
        if (window.confirm('Вы уверены, что хотите удалить это мероприятие?')) {
            try {
                await eventAPI.deleteEvent(eventId);
                navigate('/my-events', {
                    state: {
                        message: 'Мероприятие успешно удалено',
                        variant: 'success'
                    }
                });
            } catch (error) {
                console.error('Ошибка удаления:', error);
                setError('Не удалось удалить мероприятие');
            }
        }
    };

    const handleSearch = async () => {
        if (!searchQuery.trim()) {
            setSearchResults([]);
            return;
        }

        try {
            const response = await searchProfiles(searchQuery, 5);
            const currentParticipantIds = new Set(participants.map(p => p.userId));
            setSearchResults(response.filter(user => !currentParticipantIds.has(user.userId)));
        } catch (error) {
            console.error('Ошибка поиска:', error);
            setError('Ошибка при поиске пользователей');
        }
    };

    const handleAddParticipant = async (userIdToAdd) => {
        try {
            await eventAPI.addParticipant(eventId, userIdToAdd, userId);
            const updated = await eventAPI.getParticipants(eventId);
            setParticipants(updated.data);
            setSearchQuery('');
            setSearchResults([]);
        } catch (error) {
            console.error('Ошибка добавления:', error);
            setError('Не удалось добавить участника');
        }
    };

    const handleParticipantRemoved = (participantId) => {
        setParticipants(participants.filter(p => p.id !== participantId));
    };

    const handleCreateChat = async () => {
        try {
            const chatName = `Чат для ${event.title}`;
            await eventAPI.createEventChat(eventId, chatName, userId);

            setError({
                variant: 'success',
                message: 'Групповой чат успешно создан!'
            });
            setTimeout(() => setError(null), 3000);
        } catch (error) {
            console.error('Ошибка создания чата:', error);
            setError({
                variant: 'error',
                message: 'Не удалось создать чат'
            });
        }
    };

    const handleEditSuccess = (updatedEvent) => {
        setEvent(updatedEvent);
        setShowEditForm(false);
        setError({
            variant: 'success',
            message: 'Изменения сохранены успешно!'
        });
        setTimeout(() => setError(null), 3000);
    };

    const getSportIcon = (sportType) => {
        const icons = {
            'FOOTBALL': '⚽',
            'BASKETBALL': '🏀',
            'VOLLEYBALL': '🏐',
            'TENNIS': '🎾'
        };
        return icons[sportType] || '🏅';
    };

    const getStatusClass = (statusCode) => {
        const statuses = {
            'PLANNED': { class: 'planned', text: 'Запланировано' },
            'ONGOING': { class: 'ongoing', text: 'В процессе' },
            'COMPLETED': { class: 'completed', text: 'Завершено' }
        };
        return statuses[statusCode] || { class: 'default', text: statusCode };
    };

    if (loading) {
        return (
            <div className="event-loading">
                <div className="spinner"></div>
                <p>Загружаем данные мероприятия...</p>
            </div>
        );
    }

    if (error && !event) {
        return (
            <div className="event-error">
                <div className="error-card">
                    <h2>Ошибка загрузки</h2>
                    <p>{error.message || error}</p>
                    <div className="actions">
                        <button onClick={() => window.location.reload()}>Попробовать снова</button>
                        <button onClick={() => navigate('/my-events')}>К списку мероприятий</button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="event-page">
            <header className="event-header">
                <button className="back-btn" onClick={() => navigate(-1)}>
                    <i className="icon-arrow"></i> Назад
                </button>
                <h1 className="event-title">{event?.title || 'Мероприятие'}</h1>
                <div className="event-actions">
                    <button
                        className={`action-btn edit-btn ${showEditForm ? 'active' : ''}`}
                        onClick={() => setShowEditForm(!showEditForm)}
                    >
                        {showEditForm ? (
                            <>
                                <i className="icon-close"></i> Отменить
                            </>
                        ) : (
                            <>
                                <i className="icon-edit"></i> Редактировать
                            </>
                        )}
                    </button>
                    <button
                        className="action-btn delete-btn"
                        onClick={handleDelete}
                    >
                        <i className="icon-delete"></i> Удалить
                    </button>
                </div>
            </header>

            <main className="event-content">
                {error && (
                    <div className={`alert ${error.variant || 'error'}`}>
                        {error.message}
                        <button className="close-alert" onClick={() => setError(null)}>
                            &times;
                        </button>
                    </div>
                )}

                {showEditForm && event && (
                    <div className="edit-form-container">
                        <EventForm
                            initialData={event}
                            onSuccess={handleEditSuccess}
                            onCancel={() => setShowEditForm(false)}
                        />
                    </div>
                )}

                {!showEditForm && event && (
                    <section className="event-info-section">
                        <div className="event-main-card">
                            <div className="event-header-card">
                                <span className="sport-icon">{getSportIcon(event.sportType)}</span>
                                <div className="event-title-status">
                                    <h2>{event.title}</h2>
                                    <span className={`status-badge ${getStatusClass(event.statusCode).class}`}>
                                        {getStatusClass(event.statusCode).text}
                                    </span>
                                </div>
                                <button
                                    className="chat-btn"
                                    onClick={handleCreateChat}
                                >
                                    <i className="icon-chat"></i> Создать чат
                                </button>
                            </div>

                            <div className="event-description-block">
                                <h3>Описание</h3>
                                <p>{event.description || 'Описание отсутствует'}</p>
                            </div>

                            <div className="event-details-grid">
                                <div className="detail-card">
                                    <i className="icon-location"></i>
                                    <div>
                                        <h4>Место проведения</h4>
                                        <p>{event.location || 'Не указано'}</p>
                                    </div>
                                </div>
                                <div className="detail-card">
                                    <i className="icon-time"></i>
                                    <div>
                                        <h4>Начало</h4>
                                        <p>{new Date(event.startTime).toLocaleString('ru-RU')}</p>
                                    </div>
                                </div>
                                <div className="detail-card">
                                    <i className="icon-time"></i>
                                    <div>
                                        <h4>Окончание</h4>
                                        <p>{new Date(event.endTime).toLocaleString('ru-RU')}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                )}

                <section className="participants-section">
                    <div className="search-participants-card">
                        <div className="search-container">
                            <i className="icon-search"></i>
                            <input
                                type="text"
                                placeholder="Найти участников по имени..."
                                value={searchQuery}
                                onChange={(e) => setSearchQuery(e.target.value)}
                                onKeyUp={(e) => e.key === 'Enter' && handleSearch()}
                            />
                            <button
                                className="search-btn"
                                onClick={handleSearch}
                                disabled={!searchQuery.trim()}
                            >
                                Поиск
                            </button>
                        </div>

                        {searchResults.length > 0 && (
                            <div className="search-results-dropdown">
                                {searchResults.map(user => (
                                    <div key={user.userId} className="user-result-item">
                                        <div className="user-avatar">
                                            <img
                                                src={user.avatarUrl || '/default-avatar-dark.png'}
                                                alt={user.firstName}
                                            />
                                        </div>
                                        <div className="user-info">
                                            <h4>{user.firstName} {user.lastName}</h4>
                                            <span className="user-email">{user.email}</span>
                                        </div>
                                        <button
                                            className="add-user-btn"
                                            onClick={() => handleAddParticipant(user.userId)}
                                        >
                                            <i className="icon-add"></i> Добавить
                                        </button>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                    <ParticipantList
                        eventId={eventId}
                        currentUserId={userId}
                        onParticipantRemoved={handleParticipantRemoved}
                    />
                </section>
            </main>
        </div>
    );
};

export default EventPage;