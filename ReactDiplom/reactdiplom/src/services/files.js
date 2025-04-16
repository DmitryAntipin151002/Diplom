import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083/api';

const getAuthHeader = () => {
    const token = localStorage.getItem('token') || localStorage.getItem('isFirstEnterToken');
    return {
        headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'multipart/form-data'
        }
    };
};

export const uploadAvatar = async (userId, file) => {
    try {
        const formData = new FormData();
        formData.append('avatar', file);

        const response = await axios.post(
            `${API_BASE_URL}/profiles/${userId}/avatar`,
            formData,
            getAuthHeader()
        );
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Не удалось загрузить аватар');
    }
};