import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

let stompClient = null;

export const setupWebSocket = (callbacks) => {
    const token = localStorage.getItem('token');

    if (stompClient) return;

    const socket = new SockJS('http://localhost:8083/ws-messenger');
    stompClient = new Client({
        webSocketFactory: () => socket,
        connectHeaders: { Authorization: `Bearer ${token}` },
        debug: (str) => console.log('[WS]', str),
    });

    stompClient.onConnect = () => {
        stompClient.subscribe(`/topic/chat/+/messages`, (message) => {
            const msg = JSON.parse(message.body);
            callbacks.onMessage(msg);
        });
    };

    stompClient.activate();
    return stompClient;
};

export const sendMessage = (message) => {
    if (stompClient && stompClient.connected) {
        stompClient.publish({
            destination: '/app/chat.send',
            body: JSON.stringify(message),
            headers: { Authorization: `Bearer ${localStorage.getItem('isFirstEnterToken')}` }
        });
    } else {
        console.error('Соединение не установлено');
        throw new Error('WebSocket connection not established');
    }
};

export const disconnectWebSocket = () => {
    if (stompClient) {
        stompClient.deactivate();
        stompClient = null;
    }
};