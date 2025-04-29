import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import ChatList from '../Chat_Component/ChatList';
import ChatWindow from '../Chat_Component/ChatWindow';
import CreateChatModal from '../Chat_Component/CreateChatModal';
import { getChats, getChatInfo } from '../../services/chatService';
import { getMessages } from '../../services/messageService';
import { disconnectWebSocket, setupWebSocket } from '../../utils/socket';
import '../../assets/ChatStyles.css';

const ChatPage = () => {
    const { chatId } = useParams();
    const [chats, setChats] = useState([]);
    const [currentChat, setCurrentChat] = useState(null);
    const [messages, setMessages] = useState([]);
    const [showCreateModal, setShowCreateModal] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('isFirstEnterToken');
        const userId = localStorage.getItem('userId'); // Берем userId напрямую

        if (!token || !userId) {
            setError('Authentication required');
            setLoading(false);
            return;
        }

        // Загрузка чатов
        const loadData = async () => {
            try {
                const userChats = await getChats(userId);
                setChats(userChats);

                if (chatId) {
                    const chat = await getChatInfo(chatId, userId);
                    const chatMessages = await getMessages(chatId, userId);
                    setCurrentChat(chat);
                    setMessages(chatMessages);
                }
            } catch (err) {
                setError(err.message || 'Error loading data');
            } finally {
                setLoading(false);
            }
        };

        // WebSocket подключение
        const client = setupWebSocket(token, {
            onMessage: handleNewMessage,
            onChatUpdate: handleChatUpdate,
            onParticipantUpdate: handleParticipantUpdate
        });

        loadData();

        return () => {
            disconnectWebSocket();
        };
    }, [chatId]);

    const handleNewMessage = (message) => {
        if (message.chatId === currentChat?.id) {
            setMessages(prev => [...prev, message]);
        }
        setChats(prev => prev.map(chat =>
            chat.id === message.chatId ? {...chat, lastMessage: message} : chat
        ));
    };

    const handleChatUpdate = (updatedChat) => {
        setChats(prev => prev.map(chat =>
            chat.id === updatedChat.id ? updatedChat : chat
        ));
        if (currentChat?.id === updatedChat.id) {
            setCurrentChat(updatedChat);
        }
    };

    const handleParticipantUpdate = ({ chatId, participantIds, added }) => {
        setChats(prev => prev.map(chat => {
            if (chat.id === chatId) {
                return {
                    ...chat,
                    participantIds: added
                        ? [...chat.participantIds, ...participantIds]
                        : chat.participantIds.filter(id => !participantIds.includes(id))
                };
            }
            return chat;
        }));
    };

    const handleChatCreated = (newChat) => {
        setChats(prev => [...prev, newChat]);
        setCurrentChat(newChat);
    };

    if (loading) return <div className="loading">Loading chats...</div>;
    if (error) return <div className="error">{error}</div>;

    return (
        <div className="chat-container">
            <ChatList
                chats={chats}
                currentChatId={chatId}
                onCreateChat={() => setShowCreateModal(true)}
            />

            {chatId ? (
                <ChatWindow
                    chat={currentChat}
                    messages={messages}
                    userId={localStorage.getItem('userId')}
                />
            ) : (
                <div className="chat-placeholder">
                    <h2>Select a chat or create new one</h2>
                </div>
            )}

            {showCreateModal && (
                <CreateChatModal
                    onClose={() => setShowCreateModal(false)}
                    onChatCreated={handleChatCreated}
                />
            )}
        </div>
    );
};

export default ChatPage;