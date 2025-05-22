import React from 'react';
import '../styles/ChatStyles.css';

const ParticipantList = ({ participants, currentUserId }) => {
    return (
        <div className="participant-list">
            <h3>Participants</h3>
            <div className="participants-container">
                {participants.map((participantId) => (
                    <div key={participantId} className="participant-item">
                        <span className="participant-avatar">
                            {participantId === currentUserId ? '👤 You' : '👤'}
                        </span>
                        <span className="participant-id">{participantId}</span>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ParticipantList;