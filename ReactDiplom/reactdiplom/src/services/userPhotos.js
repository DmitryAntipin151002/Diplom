// services/userPhotos.js
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083';

const getAuthHeader = () => {
    const token = localStorage.getItem('authToken');
    return {
        headers: {
            Authorization: `Bearer ${token}`
        }
    };
};

export const getUserPhotos = async (userId) => {
    try {
        const response = await axios.get(
            `${API_BASE_URL}/api/users/${userId}/photos`,
            getAuthHeader()
        );
        return response.data.map(photo => ({
            ...photo,
            photoUrl: photo.photoUrl.startsWith('/api/files/')
                ? `${API_BASE_URL}${photo.photoUrl}`
                : photo.photoUrl
        }));
    } catch (error) {
        console.error('Error fetching photos:', error);
        return [];
    }
};

export const uploadPhotoFile = async (userId, file, description = '') => {
    try {
        const formData = new FormData();
        formData.append('file', file);
        if (description) formData.append('description', description);

        const response = await axios.post(
            `${API_BASE_URL}/api/users/${userId}/photos/upload`,
            formData,
            {
                headers: {
                    'Content-Type': 'multipart/form-data',
                    Authorization: `Bearer ${localStorage.getItem('authToken')}`
                }
            }
        );

        return {
            ...response.data,
            photoUrl: response.data.photoUrl.startsWith('/api/files/')
                ? `${API_BASE_URL}${response.data.photoUrl}`
                : response.data.photoUrl
        };
    } catch (error) {
        console.error('Error uploading photo:', error);
        throw error;
    }
};

export const deletePhoto = async (userId, photoId) => {
    try {
        await axios.delete(
            `${API_BASE_URL}/api/users/${userId}/photos/${photoId}`,
            getAuthHeader()
        );
    } catch (error) {
        console.error('Error deleting photo:', error);
        throw error;
    }
};

export const setProfilePhoto = async (userId, photoId) => {
    try {
        const response = await axios.patch(
            `${API_BASE_URL}/api/users/${userId}/photos/${photoId}/profile`,
            {},
            getAuthHeader()
        );
        return response.data;
    } catch (error) {
        console.error('Error setting profile photo:', error);
        throw error;
    }
};