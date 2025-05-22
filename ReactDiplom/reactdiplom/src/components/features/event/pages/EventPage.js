import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { eventAPI } from '../services/eventService';
import { searchProfiles } from '../../profile/services/ProfileService'; // Импортируем profileAPI
import EventForm from './EventForm';
import '../styles/MyEvents.css';

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

                // Запрос деталей мероприятия
                console.log('Загрузка деталей мероприятия:', eventId);
                const eventResponse = await eventAPI.getEventDetails(eventId);
                console.log('Детали мероприятия получены:', eventResponse.data);
                setEvent(eventResponse.data);

                // Запрос участников
                console.log('Загрузка участников:', eventId);
                try {
                    const participantsResponse = await eventAPI.getParticipants(eventId);
                    console.log('Участники получены:', participantsResponse.data);
                    setParticipants(participantsResponse.data);
                } catch (participantsError) {
                    console.error('Ошибка при загрузке участников:', participantsError);
                    setParticipants([]);
                    setError('Не удалось загрузить список участников, но мероприятие доступно');
                }
            } catch (error) {
                console.error('Ошибка загрузки данных мероприятия:', {
                    status: error.response?.status,
                    data: error.response?.data,
                    message: error.message
                });
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
                console.log('Удаление мероприятия:', eventId);
                await eventAPI.deleteEvent(eventId);
                navigate('/my-events');
            } catch (error) {
                console.error('Ошибка удаления:', error);
                setError('Не удалось удалить мероприятие');
            }
        }
    };
// Обновляем handleSearch

    const handleSearch = async () => {
        if (!searchQuery.trim()) {
            setSearchResults([]);
            return;
        }

        try {
            const response = await searchProfiles(searchQuery, 5);
            const currentParticipantIds = new Set(participants.map(p => p.userId));
            const filteredResults = response.filter(
                user => !currentParticipantIds.has(user.userId)
            );
            setSearchResults(filteredResults);
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

    const handleRemoveParticipant = async (participantId) => {
        if (window.confirm('Удалить участника?')) {
            try {
                console.log('Удаление участника:', participantId);
                await eventAPI.removeParticipant(eventId, participantId, userId);
                setParticipants(participants.filter(p => p.id !== participantId));
            } catch (error) {
                console.error('Ошибка удаления участника:', error);
                setError('Не удалось удалить участника');
            }
        }
    };

    const handleCreateChat = async () => {
        try {
            const chatName = `Чат для ${event.title}`;
            console.log('Создание чата:', chatName);
            await eventAPI.createEventChat(eventId, chatName, userId);
            alert('Групповой чат успешно создан!');
        } catch (error) {
            console.error('Ошибка создания чата:', error);
            setError('Не удалось создать чат');
        }
    };

    const handleEditSuccess = (updatedEvent) => {
        console.log('Мероприятие обновлено:', updatedEvent);
        setEvent(updatedEvent);
        setShowEditForm(false);
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

    if (loading) return <div className="loading">Загрузка...</div>;
    if (error && !event) return <div className="error">{error}</div>;

    return (
        <div className="my-events-container">
            <div className="content-wrapper">
                <header className="header">
                    <h1>{event?.title || 'Мероприятие'}</h1>
                </header>

                <div className="event-details">
                    {error && <div className="error">{error}</div>}
                    {event && (
                        <div className="event-info">
                            <div className="event-header">
                                <span className="sport-icon">{getSportIcon(event.sportType)}</span>
                                <h2>{event.title}</h2>
                            </div>
                            <p className="event-description">{event.description || 'Описание отсутствует'}</p>
                            <p className="event-detail">
                                <span className="icon">📍</span>
                                {event.location}
                            </p>
                            <p className="event-detail">
                                <span className="icon">⏰</span>
                                Начало: {new Date(event.startTime).toLocaleString('ru-RU')}
                            </p>
                            <p className="event-detail">
                                <span className="icon">⏰</span>
                                Окончание: {new Date(event.endTime).toLocaleString('ru-RU')}
                            </p>
                            <p className="event-detail">
                                <span className="icon">👥</span>
                                Участников: {participants.length}
                            </p>
                            <p className={`event-status ${getStatusClass(event.statusCode)}`}>
                                {event.statusCode}
                            </p>
                        </div>
                    )}

                    <div className="button-group">
                        <button
                            className="edit-button"
                            onClick={() => setShowEditForm(!showEditForm)}
                        >
                            {showEditForm ? 'Отменить редактирование' : 'Редактировать мероприятие'}
                        </button>
                        <button
                            className="delete-button"
                            onClick={handleDelete}
                        >
                            Удалить мероприятие
                        </button>
                        <button
                            className="chat-button"
                            onClick={handleCreateChat}
                        >
                            Создать групповой чат
                        </button>
                    </div>

                    {showEditForm && event && (
                        <div className="form-container">
                            <EventForm
                                initialData={event}
                                onSuccess={handleEditSuccess}
                            />
                        </div>
                    )}

                    <div className="participants-section">
                        <h3>Участники</h3>

                        <div className="search-participants">
                            <div className="search-input-group">
                                <input
                                    type="text"
                                    placeholder="Поиск по имени или фамилии"
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                    onKeyUp={(e) => e.key === 'Enter' && handleSearch()}
                                />
                                <button onClick={handleSearch} className="search-button">
                                    🔍
                                </button>
                            </div>

                            {searchResults.length > 0 && (
                                <div className="search-results">
                                    {searchResults.map(user => (
                                        <div key={user.userId} className="user-result">
                                            <img
                                                src={user.avatarUrl || '/default-avatar.png'}
                                                alt="Аватар"
                                                className="user-avatar"
                                            />
                                            <div className="user-info">
                                                <span>{user.firstName} {user.lastName}</span>
                                                <button
                                                    onClick={() => handleAddParticipant(user.userId)}
                                                    className="add-button"
                                                >
                                                    Добавить
                                                </button>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </div>

                        {participants.length === 0 ? (
                            <p className="no-participants">Участники отсутствуют</p>
                        ) : (
                            <ul className="participants-list">
                                {participants.map(participant => (
                                    <li key={participant.id} className="participant-item">
                                        <div className="participant-info">
                                            <img
                                                src={participant.userAvatar || '/default-avatar.png'}
                                                alt="Аватар"
                                                className="participant-avatar"
                                            />
                                            <span>{participant.userName}</span>
                                        </div>
                                        <button
                                            className="remove-participant-button"
                                            onClick={() => handleRemoveParticipant(participant.id)}
                                        >
                                            Удалить
                                        </button>
                                    </li>
                                ))}
                            </ul>
                        )}
                    </div>
                </div>
                );
            </div>
        </div>
    );
};

export default EventPage;