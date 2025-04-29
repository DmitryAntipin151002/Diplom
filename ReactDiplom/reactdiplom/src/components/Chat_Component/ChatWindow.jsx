import React, { useState, useEffect, useRef } from 'react';
import Message from '../Chat_Component/Message';
import MessageInput from '../Chat_Component/MessageInput';
import ParticipantList from '../Chat_Component/ParticipantList';
import { getMessages, markMessagesAsRead } from '../../services/messageService';
import { disconnectWebSocket, sendMessage as sendWsMessage, setupWebSocket } from '../../utils/socket';

const ChatWindow = ({ chat, messages, userId }) => {
    const [messageList, setMessageList] = useState(messages);
    const [isParticipantListOpen, setIsParticipantListOpen] = useState(false);
    const messagesEndRef = useRef(null);
    const currentUserId = userId || localStorage.getItem('userId');

    useEffect(() => {
        if (chat?.id && userId) {
            markMessagesAsRead(chat.id, userId).catch(console.error);
        }
    }, [messages, chat, userId]);

    useEffect(() => {
        scrollToBottom();
    }, [messageList]);

    useEffect(() => {
        const wsCallbacks = {
            onMessage: (msg) => {
                setMessageList(prev => [...prev, msg]);
            }
        };

        setupWebSocket(wsCallbacks);

        return () => disconnectWebSocket();
    }, []);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    const handleSendMessage = (messageData) => {
        try {
            sendWsMessage(messageData);
        } catch (error) {
            console.error('Failed to send message:', error);
            alert('Ошибка отправки сообщения');
        }
    };

    if (!chat) return <div className="chat-window empty">Выберите чат для начала общения</div>;

    return (
        <div className="chat-window">
            <div className="chat-header">
                <h2>{chat.name}</h2>
                <button
                    onClick={() => setIsParticipantListOpen(!isParticipantListOpen)}
                    className="participants-toggle"
                >
                    {isParticipantListOpen ? 'Скрыть участников' : `Участники (${chat.participantIds.length})`}
                </button>
            </div>

            <div className="chat-content">
                {isParticipantListOpen ? (
                    <ParticipantList
                        chatId={chat.id}
                        participants={chat.participantIds}
                        currentUserId={userId}
                    />
                ) : (
                    <>
                        <div className="messages-container">
                            {messageList.map(message => (
                                <Message
                                    key={message.id}
                                    message={message}
                                    isCurrentUser={message.senderId === userId}
                                />
                            ))}
                            <div ref={messagesEndRef} />
                        </div>
                        <MessageInput
                            onSend={handleSendMessage}
                            chat={chat}
                        />
                    </>
                )}
            </div>
        </div>
    );
};

export default ChatWindow;