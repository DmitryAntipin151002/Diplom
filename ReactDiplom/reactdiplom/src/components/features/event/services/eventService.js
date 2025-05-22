import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083';

const getAuthConfig = () => {
    const token = localStorage.getItem('isFirstEnterToken');
    if (!token) {
        console.error('Токен авторизации отсутствует');
    }
    return {
        headers: {
            Authorization: `Bearer ${token}`,
            'X-User-Id': localStorage.getItem('userId') || ''
        }
    };
};

export const eventAPI = {
    createEvent: (organizerId, data) => {
        console.log('Создание события:', { organizerId, data });
        return axios.post(`${API_BASE_URL}/api/events`, data, {
            ...getAuthConfig(),
            headers: {
                ...getAuthConfig().headers,
                'X-User-Id': String(organizerId)
            }
        });
    },

    getUpcomingEvents: () => {
        console.log('Запрос предстоящих событий');
        return axios.get(`${API_BASE_URL}/api/events/upcoming`, getAuthConfig());
    },

    getMyEvents: (organizerId) => {
        console.log('Запрос событий организатора:', organizerId);
        return axios.get(`${API_BASE_URL}/api/events/organizer/${organizerId}`, getAuthConfig());
    },

    updateEventStatus: (eventId, status) => {
        console.log('Обновление статуса события:', { eventId, status });
        return axios.patch(`${API_BASE_URL}/api/events/${eventId}/status?status=${status}`, null, getAuthConfig());
    },

    deleteEvent: (eventId) => {
        console.log('Удаление события:', eventId);
        return axios.delete(`${API_BASE_URL}/api/events/${eventId}`, getAuthConfig());
    },

    getEventDetails: (eventId) => {
        console.log('Запрос деталей события:', eventId);
        return axios.get(`${API_BASE_URL}/api/events/${eventId}`, getAuthConfig());
    },

    getParticipants: (eventId) => {
        console.log('Запрос участников события:', eventId);
        return axios.get(`${API_BASE_URL}/api/events/${eventId}/participants`, getAuthConfig())
            .catch(error => {
                console.error('Ошибка в getParticipants:', {
                    status: error.response?.status,
                    data: error.response?.data,
                    message: error.message
                });
                throw error;
            });
    },

    addParticipant: (eventId, userId, organizerId) => {
        console.log('Добавление участника:', { eventId, userId, organizerId });
        return axios.post(
            `${API_BASE_URL}/api/events/${eventId}/participants`,
            null,
            {
                ...getAuthConfig(),
                headers: { ...getAuthConfig().headers, 'X-User-Id': String(organizerId) },
                params: { userId }
            }
        );
    },

    removeParticipant: (eventId, participantId, requesterId) => {
        console.log('Удаление участника:', { eventId, participantId, requesterId });
        return axios.delete(
            `${API_BASE_URL}/api/events/${eventId}/participants/${participantId}`,
            {
                ...getAuthConfig(),
                headers: { ...getAuthConfig().headers, 'X-User-Id': String(requesterId) }
            }
        );
    },

    getAllEvents: () => {
        return axios.get(`${API_BASE_URL}/api/events`, getAuthConfig());
    },

    createEventChat: (eventId, chatName, creatorId) => {
        console.log('Создание чата:', { eventId, chatName, creatorId });
        return axios.post(
            `${API_BASE_URL}/api/chats/event/${eventId}?chatName=${encodeURIComponent(chatName)}`,
            null, // Тело запроса пустое
            {
                headers: {
                    ...getAuthConfig().headers,
                    'X-User-Id': String(creatorId)
                }
            }
        );
    }
};