import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083';

const getAuthConfig = () => {
    const token = localStorage.getItem('isFirstEnterToken');
    return {
        headers: {
            Authorization: `Bearer ${token}`
        }
    };
};

export const getMessages = async (chatId, userId, page = 0, size = 50) => {
    try {
        const response = await axios.get(
            `${API_BASE_URL}/api/chats/${chatId}/messages?userId=${userId}&page=${page}&size=${size}`,
            getAuthConfig()
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to fetch messages');
    }
};


export const markMessagesAsRead = async (chatId, userId) => {
    try {
        await axios.post(
            `${API_BASE_URL}/api/chats/${chatId}/messages/read?userId=${userId}`,
            {},
            getAuthConfig()
        );
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to mark messages as read');
    }
};

export const editMessage = async (messageId, newContent) => {
    const editorId = localStorage.getItem('userId'); // Берем ID из localStorage
    try {
        const response = await axios.put(
            `${API_BASE_URL}/api/messages/${messageId}`,
            { newContent, editorId }, // Тело запроса
            getAuthConfig()
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to edit message');
    }
};

export const deleteMessage = async (messageId) => {
    const deleterId = localStorage.getItem('userId'); // Берем ID из localStorage
    try {
        await axios.delete(
            `${API_BASE_URL}/api/messages/${messageId}`,
            {
                ...getAuthConfig(),
                data: { deleterId } // Для DELETE передаем тело через data
            }
        );
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to delete message');
    }
};