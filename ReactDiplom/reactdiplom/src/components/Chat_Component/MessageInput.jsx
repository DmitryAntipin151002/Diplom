import React, { useState } from 'react';
import './../../assets/ChatStyles.css';

const MessageInput = ({ onSend, chat }) => {
    const [message, setMessage] = useState('');
    const userId = localStorage.getItem('userId');

    const handleSubmit = (e) => {
        e.preventDefault();
        const trimmedMessage = message.trim();

        if (!trimmedMessage || !userId || !chat?.id) {
            alert("Сообщение не может быть пустым");
            return;
        }

        try {
            const messageData = {
                chatId: chat.id,
                senderId: userId,
                content: trimmedMessage,
                timestamp: new Date().toISOString()
            };

            onSend(messageData);
            setMessage('');
        } catch (error) {
            console.error('Ошибка отправки:', error);
            alert(`Ошибка отправки: ${error.message}`);
        }
    };

    return (
        <form className="message-input-form" onSubmit={handleSubmit}>
            <div className="input-container">
                <input
                    type="text"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder="Введите сообщение..."
                    disabled={!chat}
                />
                <button
                    type="submit"
                    disabled={!chat || !message.trim()}
                >
                    Отправить
                </button>
            </div>
        </form>
    );
};

export default MessageInput;