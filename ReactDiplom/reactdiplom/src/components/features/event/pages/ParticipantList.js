// components/ParticipantList.jsx
import React, { useEffect, useState } from 'react';
import { eventAPI } from '../services/eventService';

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

    if (loading) return (
        <div className="loading-container">
            <div className="loading-spinner"></div>
            <p>Загрузка участников...</p>
        </div>
    );

    if (error) return (
        <div className="error-message">
            <svg className="error-icon" viewBox="0 0 24 24">
                <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/>
            </svg>
            <span>{error}</span>
        </div>
    );

    return (
        <div className="participants-container">
            <div className="participants-header">
                <h3>Участники мероприятия</h3>
                <span className="participants-count">{participants.length}</span>
            </div>

            {participants.length === 0 ? (
                <div className="empty-state">
                    <svg className="empty-icon" viewBox="0 0 24 24">
                        <path d="M12 5.9c1.16 0 2.1.94 2.1 2.1s-.94 2.1-2.1 2.1S9.9 9.16 9.9 8s.94-2.1 2.1-2.1m0 9c2.97 0 6.1 1.46 6.1 2.1v1.1H5.9V17c0-.64 3.13-2.1 6.1-2.1M12 4C9.79 4 8 5.79 8 8s1.79 4 4 4 4-1.79 4-4-1.79-4-4-4zm0 9c-2.67 0-8 1.34-8 4v3h16v-3c0-2.66-5.33-4-8-4z"/>
                    </svg>
                    <p>Пока нет участников</p>
                </div>
            ) : (
                <ul className="participants-grid">
                    {participants.map(participant => (
                        <li key={participant.id} className="participant-card">
                            <div className="participant-avatar-container">
                                <img
                                    src={participant.userAvatar || '/default-avatar-dark.png'}
                                    alt="Аватар"
                                    className="participant-avatar"
                                    onError={(e) => {
                                        e.target.onerror = null;
                                        e.target.src = '/default-avatar-dark.png';
                                    }}
                                />
                            </div>
                            <div className="participant-info">
                                <h4 className="participant-name">{participant.userName}</h4>
                                <button
                                    className="remove-button"
                                    onClick={() => handleRemoveParticipant(participant.id)}
                                    aria-label="Удалить участника"
                                >
                                    <svg className="remove-icon" viewBox="0 0 24 24">
                                        <path d="M19 13H5v-2h14v2z"/>
                                    </svg>
                                </button>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default ParticipantList;