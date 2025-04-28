import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

let stompClient = null;
let socket = null;

export const setupWebSocket = (token, callbacks) => {
    // 1. Убираем дублирующее объявление stompClient

    if (stompClient) {
        disconnectWebSocket();
    }

    const socket = new SockJS('http://localhost:8083/ws-messenger');
    stompClient = new Client({
        webSocketFactory: () => socket,
        connectHeaders: {
            Authorization: `Bearer ${token}`
        },
        debug: (str) => console.log(str),
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000
    });

    // 2. Исправляем дублирование onConnect
    stompClient.onConnect = (frame) => {
        console.log('Connected to WebSocket');

        // Подписка на ошибки
        stompClient.subscribe('/user/queue/errors', (message) => {
            console.error('WebSocket Error:', message.body);
        });

        // Остальные подписки
        stompClient.subscribe(`/topic/chat/+/messages`, (message) => {
            const msg = JSON.parse(message.body);
            callbacks.onMessage(msg);
        });

        stompClient.subscribe(`/topic/chat/+/updates`, (chat) => {
            const updatedChat = JSON.parse(chat.body);
            callbacks.onChatUpdate(updatedChat);
        });

        stompClient.subscribe(`/topic/chat/+/participants`, (update) => {
            const participantUpdate = JSON.parse(update.body);
            callbacks.onParticipantUpdate(participantUpdate);
        });

        stompClient.subscribe(`/user/queue/chat-updates`, (notification) => {
            const notif = JSON.parse(notification.body);
            console.log('Personal notification:', notif);
        });
    };

    stompClient.onStompError = (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
    };

    // 3. Добавляем обработчик ошибок подключения
    stompClient.onWebSocketError = (error) => {
        console.error('WebSocket Error:', error);
    };

    stompClient.activate();
    return stompClient;
};

export const sendMessage = (message) => {
    if (stompClient && stompClient.connected) {
        stompClient.publish({
            destination: '/app/chat.send',
            body: JSON.stringify(message)
        });
    } else {
        console.error('WebSocket connection not established');
    }
};

export const disconnectWebSocket = () => {
    if (stompClient) {
        stompClient.deactivate().then(() => {
            console.log('WebSocket connection closed');
            stompClient = null;
            socket = null;
        });
    }
};