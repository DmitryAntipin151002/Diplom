import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083';

export const getUserPhotos = async (userId) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/api/users/${userId}/photos`);
        return Array.isArray(response.data) ? response.data : [];
    } catch (error) {
        console.error('Error fetching photos:', error);
        return [];
    }
};

export const uploadPhotoFile = async (userId, formData) => {
    try {
        const response = await axios.post(
            `${API_BASE_URL}/api/users/${userId}/photos/upload`,
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            }
        );
        return response.data;
    } catch (error) {
        console.error('Error uploading photo:', error);
        throw error;
    }
};

export const deletePhoto = async (userId, photoId) => {
    try {
        await axios.delete(`${API_BASE_URL}/api/users/${userId}/photos/${photoId}`);
    } catch (error) {
        console.error('Error deleting photo:', error);
        throw error;
    }
};

export const setProfilePhoto = async (userId, photoId) => {
    try {
        const response = await axios.patch(
            `${API_BASE_URL}/api/users/${userId}/photos/${photoId}/profile`
        );
        return response.data;
    } catch (error) {
        console.error('Error setting profile photo:', error);
        throw error;
    }
};