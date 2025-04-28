import React, { useState, useEffect, useRef } from 'react';
import Message from '../Chat_Component/Message';
import MessageInput from '../Chat_Component/MessageInput';
import ParticipantList from '../Chat_Component/ParticipantList';
import { getMessages, sendMessage, markMessagesAsRead } from '../../services/messageService';
import { sendMessage as sendWsMessage } from '../../utils/socket';

const ChatWindow = ({ chat, messages, userId }) => {
    const [messageList, setMessageList] = useState(messages);
    const [isParticipantListOpen, setIsParticipantListOpen] = useState(false);
    const messagesEndRef = useRef(null);

    useEffect(() => {
        setMessageList(messages);
        if (chat?.id) {
            markMessagesAsRead(chat.id, userId).catch(console.error);
        }
    }, [messages, chat, userId]);

    useEffect(() => {
        scrollToBottom();
    }, [messageList]);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    const handleSendMessage = (content) => {
        const messageRequest = {
            chatId: chat.id,
            senderId: userId,
            content: content
        };
        sendWsMessage(messageRequest); // Только через WebSocket
    };

    if (!chat) return <div className="chat-window empty">Select a chat to start messaging</div>;

    return (
        <div className="chat-window">
            <div className="chat-header">
                <h2>{chat.name}</h2>
                <button
                    onClick={() => setIsParticipantListOpen(!isParticipantListOpen)}
                    className="participants-toggle"
                >
                    {isParticipantListOpen ? 'Hide Participants' : `Show Participants (${chat.participantIds.length})`}
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
                        <MessageInput onSend={handleSendMessage} />
                    </>
                )}
            </div>
        </div>
    );
};

export default ChatWindow;