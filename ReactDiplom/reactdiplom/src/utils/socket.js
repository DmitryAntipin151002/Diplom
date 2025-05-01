// utils/socket.js
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

let stompClient = null;
let reconnectAttempts = 0;
const MAX_RECONNECT_ATTEMPTS = 5;
const RECONNECT_DELAY = 5000;

export const setupWebSocket = (callbacks) => {
    const token = localStorage.getItem('isFirstEnterToken');

    if (stompClient && stompClient.connected) {
        return stompClient;
    }

    const socket = new SockJS('http://localhost:8083/ws-messenger');
    stompClient = new Client({
        webSocketFactory: () => socket,
        connectHeaders: { Authorization: `Bearer ${token}` },
        debug: (str) => console.log('[WS]', str),
        reconnectDelay: RECONNECT_DELAY,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
    });

    stompClient.onConnect = (frame) => {
        console.log('WebSocket connected');

        // Подписка на обновления чатов
        stompClient.subscribe(`/user/${getCurrentUserId()}/queue/chat-updates`, (message) => {
            const update = JSON.parse(message.body);
            callbacks.onChatUpdate && callbacks.onChatUpdate(update);
        });

        // Подписка на новые сообщения
        stompClient.subscribe(`/user/${getCurrentUserId()}/queue/messages`, (message) => {
            const msg = JSON.parse(message.body);
            callbacks.onMessage && callbacks.onMessage(msg);
        });

        // Подписка на обновления статусов
        stompClient.subscribe(`/topic/status`, (message) => {
            const statusUpdate = JSON.parse(message.body);
            callbacks.onStatusUpdate && callbacks.onStatusUpdate(statusUpdate);
        });
    };

    stompClient.onStompError = (frame) => {
        console.error('WebSocket error:', frame.headers.message);
    };

    stompClient.onWebSocketClose = () => {
        console.log('WebSocket disconnected');
        if (reconnectAttempts < MAX_RECONNECT_ATTEMPTS) {
            reconnectAttempts++;
            setTimeout(() => {
                console.log(`Attempting to reconnect (${reconnectAttempts}/${MAX_RECONNECT_ATTEMPTS})`);
                stompClient.activate();
            }, 5000);
        }
    };

    stompClient.activate();
    return stompClient;
};

const getCurrentUserId = () => {
    return localStorage.getItem('userId');
};

export const sendMessage = (message) => {
    if (!stompClient || !stompClient.connected) {
        throw new Error('Connection not established');
    }

    try {
        stompClient.publish({
            destination: '/app/chat.send',
            body: JSON.stringify(message),
            headers: {
                'content-type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
    } catch (error) {
        console.error('Error sending message:', error);
        throw error;
    }
};

export const disconnectWebSocket = () => {
    if (stompClient) {
        stompClient.deactivate();
        stompClient = null;
    }
};

export const subscribeToChat = (chatId) => {
    if (!stompClient || !stompClient.connected) {
        throw new Error('WebSocket connection not established');
    }

    return stompClient.subscribe(`/topic/chat/${chatId}`, (message) => {
        const msg = JSON.parse(message.body);
        // Обработка сообщений конкретного чата
    });
};