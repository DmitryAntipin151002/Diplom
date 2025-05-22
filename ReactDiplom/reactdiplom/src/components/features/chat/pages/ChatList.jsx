
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './../styles/ChatStyles.css';

const ChatList = ({ chats, currentChatId, onCreateChat }) => {
    const navigate = useNavigate();
    const userId = localStorage.getItem('userId');

    const handleChatClick = (chatId) => {
        navigate(`/chats/${chatId}`);
    };

    const formatLastMessage = (message) => {
        if (!message || !message.content) return 'No messages yet';
        return message.content.length > 30
            ? `${message.content.substring(0, 30)}...`
            : message.content;
    };

    return (
        <div className="chat-list-container">
            <div className="chat-list-header">
                <h2>Chats</h2>
                <button onClick={onCreateChat} className="new-chat-button">
                    + New Chat
                </button>
            </div>

            <div className="chats-scrollable">
                {chats.map(chat => (
                    <div
                        key={chat.id}
                        className={`chat-item ${chat.id === currentChatId ? 'active' : ''}`}
                        onClick={() => handleChatClick(chat.id)}
                    >
                        <div className="chat-avatar">
                            {chat.type === 'GROUP' ? '👥' : '👤'}
                        </div>

                        <div className="chat-info">
                            <div className="chat-header">
                                <h3 className="chat-name">{chat.name}</h3>
                                <span className="chat-time">
                                    {chat.lastMessage?.sentAt
                                        ? new Date(chat.lastMessage.sentAt).toLocaleTimeString([], {
                                            hour: '2-digit',
                                            minute: '2-digit'
                                        })
                                        : '--:--'}
                                </span>
                            </div>

                            <div className="chat-preview">
                                <p className="last-message">
                                    {formatLastMessage(chat.lastMessage)}
                                </p>
                                {chat.unreadCount > 0 && (
                                    <span className="unread-count">{chat.unreadCount}</span>
                                )}
                            </div>
                        </div>
                    </div>
                ))}

                {chats.length === 0 && (
                    <div className="no-chats">
                        <p>No chats found. Start a new conversation!</p>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ChatList;