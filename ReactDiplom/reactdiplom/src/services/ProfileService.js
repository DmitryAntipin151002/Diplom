import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083/api';

const getAuthHeader = () => {
    const token = localStorage.getItem('token') || localStorage.getItem('isFirstEnterToken');
    return {
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    };
};

export const getProfile = async (userId) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/profiles/${userId}`, getAuthHeader());
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Не удалось загрузить профиль');
    }
};

export const updateProfile = async (userId, data) => {
    try {
        const response = await axios.put(
            `${API_BASE_URL}/profiles/${userId}`,
            {
                ...data,
                sportType: data.sportType // Явно указываем поле
            },
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Не удалось обновить профиль');
    }
};

export const getUserStats = async (userId) => {
    try {
        const response = await axios.get(
            `${API_BASE_URL}/profiles/${userId}/stats`,
            getAuthHeader()
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Не удалось загрузить статистику');
    }
};

export const uploadAvatar = async (userId, file) => {
    const formData = new FormData();
    formData.append('file', file);

    const response = await axios.post(
        `${API_BASE_URL}/api/users/${userId}/avatar`,
        formData,
        {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        }
    );
    return response.data.avatarUrl;
};

export const createProfileIfNotExists = async (userId) => {
    try {
        const response = await axios.post(
            `${API_BASE_URL}/profiles/${userId}/create`,
            {},
            getAuthHeader()
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Не удалось создать профиль');
    }
};