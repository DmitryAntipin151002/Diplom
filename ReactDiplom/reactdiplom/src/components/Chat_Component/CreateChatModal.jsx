import React, { useState, useEffect } from 'react';
import { createChat } from '../../services/chatService';
import FriendService from '../../services/FriendService';
import './../../assets/ChatStyles.css';

const CreateChatModal = ({ onClose, onChatCreated }) => {
    const [chatName, setChatName] = useState('');
    const [selectedFriends, setSelectedFriends] = useState([]);
    const [friends, setFriends] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const userId = localStorage.getItem('userId');

    useEffect(() => {
        const loadFriends = async () => {
            try {
                const friendsList = await FriendService.getFriends(userId);
                // Убедимся, что у каждого друга есть relatedUserId
                const validFriends = friendsList.filter(friend => friend.relatedUserId);
                setFriends(validFriends);
                setLoading(false);
            } catch (err) {
                setError(err.message || 'Failed to load friends');
                setLoading(false);
            }
        };
        loadFriends();
    }, [userId]);

    const handleFriendToggle = (friendId) => {
        if (!friendId) return; // Игнорируем null/undefined

        setSelectedFriends(prev =>
            prev.includes(friendId)
                ? prev.filter(id => id !== friendId)
                : [...prev, friendId]
        );
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (selectedFriends.length === 0) {
            setError('Please select at least one friend');
            return;
        }

        try {
            const chat = await createChat({
                creatorId: userId, // <-- Добавлено поле
                name: chatName || `Chat with ${selectedFriends.length} friends`,
                type: selectedFriends.length > 1 ? 'GROUP' : 'PRIVATE',
                participantIds: selectedFriends,
            });
            onChatCreated(chat);
            onClose();
        } catch (error) {
            setError(error.message || 'Failed to create chat');
            console.error('Failed to create chat:', error);
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content cyber-form">
                <button onClick={onClose} className="close-button">×</button>
                <h2 className="neon-heading">Create New Chat</h2>

                {error && <div className="cyber-error" style={{marginBottom: '1rem'}}>{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label className="input-label">Chat Name (optional)</label>
                        <input
                            type="text"
                            className="cyber-input"
                            value={chatName}
                            onChange={(e) => setChatName(e.target.value)}
                            placeholder="Enter chat name"
                        />
                    </div>

                    <div className="form-group">
                        <label className="input-label">Select Friends</label>
                        {loading ? (
                            <div>Loading friends...</div>
                        ) : (
                            <div className="friends-select">
                                {friends.map(friend => (
                                    <div key={friend.relatedUserId} className="friend-select-item">
                                        <label>
                                            <input
                                                type="checkbox"
                                                checked={selectedFriends.includes(friend.relatedUserId)}
                                                onChange={() => handleFriendToggle(friend.relatedUserId)}
                                            />
                                            {friend.relatedUser?.profile?.firstName || 'Unknown User'} {friend.relatedUser?.profile?.lastName || ''}
                                        </label>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                    <div className="form-actions">
                        <button type="button" onClick={onClose} className="cyber-button cancel">
                            Cancel
                        </button>
                        <button
                            type="submit"
                            className="cyber-button submit"
                            disabled={selectedFriends.length === 0}
                        >
                            Create Chat
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default CreateChatModal;