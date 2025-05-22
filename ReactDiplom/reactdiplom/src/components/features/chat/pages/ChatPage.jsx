import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ChatList from './ChatList';
import ChatWindow from './ChatWindow';
import CreateChatModal from './CreateChatModal';
import { getChats, getChatInfo } from './../services/chatService';
import { getMessages } from '../services/messageService';
import { disconnectWebSocket, setupWebSocket } from '../../../../utils/socket';
import '../styles/ChatStyles.css';

const ChatPage = () => {
    const { chatId } = useParams();
    const navigate = useNavigate();
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
            navigate('/login');
            return;
        }

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

        const client = setupWebSocket(token, {
            onMessage: (message) => {
                if (message.chatId === currentChat?.id) {
                    setMessages(prev => [...prev, message]);
                }
                setChats(prev => prev.map(chat =>
                    chat.id === message.chatId ? {...chat, lastMessage: message} : chat
                ));
            },
            onChatUpdate: (updatedChat) => {
                setChats(prev => prev.map(chat =>
                    chat.id === updatedChat.id ? updatedChat : chat
                ));
                if (currentChat?.id === updatedChat.id) {
                    setCurrentChat(updatedChat);
                }
            },
            onParticipantUpdate: ({ chatId, participantIds, added }) => {
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
            }
        });

        loadData();

        return () => {
            disconnectWebSocket();
        };
    }, [chatId, navigate]);

    const handleChatCreated = (newChat) => {
        setChats(prev => [...prev, newChat]);
        setCurrentChat(newChat);
        navigate(`/chats/${newChat.id}`);
    };

    if (loading) return <div className="cyber-loader">Loading chats...</div>;
    if (error) return <div className="cyber-error">{error}</div>;

    return (
        <div className="cyber-chat-container">
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
                <div className="cyber-chat-placeholder">
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