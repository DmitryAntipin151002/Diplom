import React, { useState, useEffect, useRef } from 'react';
import Message from '../Chat_Component/Message';
import MessageInput from '../Chat_Component/MessageInput';
import ParticipantList from '../Chat_Component/ParticipantList';
import { getMessages, markMessagesAsRead } from '../../services/messageService';
import {disconnectWebSocket, sendMessage as sendWsMessage, setupWebSocket} from '../../utils/socket';
import * as tempMessage from "date-fns/locale";

const ChatWindow = ({ chat, messages, userId }) => {
    const [messageList, setMessageList] = useState(messages);
    const [isParticipantListOpen, setIsParticipantListOpen] = useState(false);
    const messagesEndRef = useRef(null);

    useEffect(() => {
        setMessageList(messages);
    }, [messages]);

    useEffect(() => {
        if (chat?.id && userId) {
            markMessagesAsRead(chat.id, userId).catch(console.error);
        }
    }, [messages, chat, userId]);

    useEffect(() => {
        const loadMessages = async () => {
            const messages = await getMessages(chat.id, userId);
            setMessageList(messages.sort((a, b) =>
                new Date(a.sentAt) - new Date(b.sentAt)
            ));
        };

        if (chat?.id) loadMessages();
    }, [chat?.id]);

    useEffect(() => {
        const onNewMessage = (newMessage) => {
            setMessageList(prev => {
                // Обновляем статус для оптимистичных сообщений
                if (newMessage.tempId) {
                    return prev.map(msg =>
                        msg.tempId === newMessage.tempId ? { ...newMessage, status: 'SENT' } : msg
                    );
                }
                // Добавляем новые сообщения с сервера
                return [...prev, newMessage];
            });
        };

        const handleDeleteMessage = (messageId, restore = false, confirmed = false) => {
            setMessageList(prev => {
                if (restore) {
                    // Восстановление сообщения
                    const original = messages.find(m => m.id === messageId);
                    return original ? [...prev, original] : prev;
                }

                if (confirmed) {
                    // Подтвержденное удаление - полное удаление
                    return prev.filter(m => m.id !== messageId);
                }

                // Оптимистичное удаление с пометкой
                return prev.map(m =>
                    m.id === messageId ? {...m, status: 'PENDING_DELETE'} : m
                );
            });
        };

        const onMessageUpdate = (updatedMessage) => {
            setMessageList(prev =>
                prev.map(msg => msg.id === updatedMessage.id ? updatedMessage : msg)
            );
        };

        setupWebSocket({
            onMessage: onNewMessage,
            onChatUpdate: onMessageUpdate
        });

        return () => disconnectWebSocket();
    }, []);

    useEffect(() => {
        scrollToBottom();
    }, [messageList]);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    const handleSendMessage = async (messageData) => {
        try {
            // Оптимистичное обновление UI
            const tempMessage = {
                ...messageData,
                id: Date.now().toString(), // временный ID
                sentAt: new Date().toISOString(),
                status: 'SENDING'
            };

            setMessageList(prev => [...prev, tempMessage]);
            scrollToBottom();

            // Отправка через WebSocket
            await sendWsMessage(messageData);

            // Обновляем статус сообщения после успешной отправки
            setMessageList(prev => prev.map(msg =>
                msg.id === tempMessage.id ? {...msg, status: 'SENT'} : msg
            ));
        } catch (error) {
            console.error('Failed to send message:', error);
            setMessageList(prev => prev.map(msg =>
                msg.id === tempMessage.id ? {...msg, status: 'ERROR'} : msg
            ));
        }
    };

    if (!chat) return <div className="cyber-chat-window empty">Select a chat to start messaging</div>;

    return (
        <div className="cyber-chat-window">
            <div className="cyber-chat-header">
                <h2>{chat.name}</h2>
                <button
                    onClick={() => setIsParticipantListOpen(!isParticipantListOpen)}
                    className="cyber-button participants-toggle"
                >
                    {isParticipantListOpen ? 'Hide participants' : `Participants (${chat.participantIds.length})`}
                </button>
            </div>

            <div className="cyber-chat-content">
                {isParticipantListOpen ? (
                    <ParticipantList
                        chatId={chat.id}
                        participants={chat.participantIds}
                        currentUserId={userId}
                    />
                ) : (
                    <>
                        <div className="cyber-messages-container">
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