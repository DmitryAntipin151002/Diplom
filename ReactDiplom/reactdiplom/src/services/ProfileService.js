// services/ProfileService.js
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083';

const getAuthHeader = () => {
    const token = localStorage.getItem('token') || localStorage.getItem('authToken');
    return {
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    };
};

export const getProfile = async (userId) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/api/profiles/${userId}`, getAuthHeader());
        return {
            ...response.data,
            avatarUrl: response.data.avatarUrl
                ? `${API_BASE_URL}${response.data.avatarUrl}`
                : `${API_BASE_URL}/default-avatar.png`
        };
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to load profile');
    }
};

export const updateProfile = async (userId, data) => {
    try {
        const response = await axios.put(
            `${API_BASE_URL}/api/profiles/${userId}`,
            data,
            getAuthHeader()
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to update profile');
    }
};

export const getUserStats = async (userId) => {
    try {
        const response = await axios.get(
            `${API_BASE_URL}/api/profiles/${userId}/stats`,
            getAuthHeader()
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to load stats');
    }
};

export const uploadAvatar = async (userId, file) => {
    try {
        const formData = new FormData();
        formData.append('avatar', file);

        const response = await axios.post(
            `${API_BASE_URL}/api/profiles/${userId}/avatar`,
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${localStorage.getItem('authToken')}`
                }
            }
        );
        return `${API_BASE_URL}${response.data}`;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to upload avatar');
    }
};

export const createProfileIfNotExists = async (userId) => {
    try {
        const response = await axios.post(
            `${API_BASE_URL}/api/profiles/${userId}/create`,
            {},
            getAuthHeader()
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to create profile');
    }
};

export const setAvatarFromGallery = async (userId, photoId) => {
    try {
        const response = await axios.patch(
            `${API_BASE_URL}/api/profiles/${userId}/avatar-from-gallery/${photoId}`,
            {},
            getAuthHeader()
        );
        return `${API_BASE_URL}${response.data}`;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Failed to set avatar from gallery');
    }
};

    export const searchProfiles = async (query, limit = 10) => {
        try {
            const response = await axios.get(
                `${API_BASE_URL}/api/profiles/search`,
                {
                    ...getAuthHeader(),
                    params: {
                        query: query,
                        limit: limit
                    }
                }
            );

            return response.data.map(profile => ({
                ...profile,
                avatarUrl: profile.avatarUrl
                    ? `${API_BASE_URL}${profile.avatarUrl}`
                    : `${API_BASE_URL}/default-avatar.png`
            }));
        } catch (error) {
            throw new Error(error.response?.data?.message || 'Failed to search profiles');
        }
    };
