// ProfileService.js (обновленная версия)
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083';

const getAuthHeader = () => {
    const token = localStorage.getItem('isFirstEnterToken');
    return { Authorization: `Bearer ${token}` };
};

const handleResponse = (response) => {
    if (response.status >= 200 && response.status < 300) {
        return response.data;
    }
    throw new Error(response.statusText);
};

const handleError = (error) => {
    if (error.response) {
        throw new Error(error.response.data.message || error.message);
    }
    throw new Error(error.message || 'Ошибка подключения');
};

const ProfileService = {
    getProfile: (userId) => axios.get(`${API_BASE_URL}/user-profiles/${userId}`, {
        headers: getAuthHeader()
    }).then(handleResponse).catch(handleError),

    updateProfile: (userId, data) => axios.put(
        `${API_BASE_URL}/user-profiles/${userId}`,
        data,
        { headers: getAuthHeader() }
    ).then(handleResponse).catch(handleError),

    uploadAvatar: (userId, file) => {
        const formData = new FormData();
        formData.append('file', file);
        return axios.post(
            `${API_BASE_URL}/user-profiles/${userId}/avatar`,
            formData,
            {
                headers: {
                    ...getAuthHeader(),
                    'Content-Type': 'multipart/form-data'
                }
            }
        ).then(handleResponse).catch(handleError);
    },

    getUserActivities: (userId) => axios.get(
        `${API_BASE_URL}/user-profiles/${userId}/activities`,
        { headers: getAuthHeader() }
    ).then(handleResponse).catch(handleError),

    getUserPhotos: (userId) => axios.get(
        `${API_BASE_URL}/user-profiles/${userId}/photos`,
        { headers: getAuthHeader() }
    ).then(handleResponse).catch(handleError)
};

export default ProfileService;