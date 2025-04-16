import axios from 'axios';

const API_URL = 'http://localhost:8083/api/users';

export const getUserPhotos = async (userId) => {
    return await axios.get(`${API_URL}/${userId}/photos`);
};

export const uploadPhoto = async (userId, photoUrl, description) => {
    return await axios.post(`${API_URL}/${userId}/photos`, { photoUrl, description });
};

export const deletePhoto = async (userId, photoId) => {
    return await axios.delete(`${API_URL}/${userId}/photos/${photoId}`);
};

export const setProfilePhoto = async (userId, photoId) => {
    return await axios.patch(`${API_URL}/${userId}/photos/${photoId}/profile`);
};

export const uploadPhotoFile = async (userId, file, description) => {
    const formData = new FormData();
    formData.append('file', file);
    if (description) formData.append('description', description);

    return await axios.post(`${API_URL}/${userId}/photos/upload`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
};