import React, { useEffect, useState } from 'react';
import { eventAPI } from '../../services/eventService';

const ParticipantList = ({ eventId }) => {
    const [participants, setParticipants] = useState([]);

    useEffect(() => {
        const loadParticipants = async () => {
            try {
                const response = await eventAPI.getParticipants(eventId);
                setParticipants(response.data);
            } catch (error) {
                console.error('Ошибка загрузки участников:', error);
            }
        };
        loadParticipants();
    }, [eventId]);

    return (
        <div className="participants-list">
            <h3>Участники</h3>
            {participants.map(participant => (
                <div key={participant.id} className="participant-info">
                    <img
                        src={participant.userAvatar || '/default-avatar.png'}
                        alt="Аватар"
                        className="avatar"
                    />
                    <div>
                        <p>{participant.userName}</p>
                        <small>{participant.location}</small>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default ParticipantList;