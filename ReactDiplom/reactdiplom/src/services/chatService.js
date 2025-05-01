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

export const getChats = async (userId) => {
    try {
        console.log('API call to get chats for:', userId);
        const response = await axios.get(
            `${API_BASE_URL}/api/chats/user/${userId}`,
            getAuthConfig()
        );
        return response.data;
    } catch (error) {
        console.error('API Error:', {
            status: error.response?.status,
            data: error.response?.data
        });
        throw error;
    }
};

export const getChatInfo = async (chatId, userId) => {
    try {
        const response = await axios.get(
            `${API_BASE_URL}/api/chats/${chatId}?userId=${userId}`,
            getAuthConfig()
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to fetch chat info');
    }
};

export const createChat = async (request) => {
    try {
        const response = await axios.post(
            `${API_BASE_URL}/api/chats`,
            request,
            getAuthConfig()
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to create chat');
    }
};

export const addParticipants = async (chatId, participantIds, requesterId) => {
    try {
        await axios.post(
            `${API_BASE_URL}/api/chats/${chatId}/participants`,
            participantIds,
            {
                ...getAuthConfig(),
                params: { requesterId }
            }
        );
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to add participants');
    }
};

export const removeParticipant = async (chatId, participantId, requesterId) => {
    try {
        await axios.delete(
            `${API_BASE_URL}/api/chats/${chatId}/participants/${participantId}`,
            {
                ...getAuthConfig(),
                params: { requesterId }
            }
        );
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to remove participant');
    }
};