import axios from 'axios';

const API_URL = 'http://localhost:8080/api/profile';

export const getProfile = async (email) => {
    const response = await axios.get(`${API_URL}?email=${email}`);
    return response.data;
};

export const updateProfile = async (email, data) => {
    const response = await axios.put(`${API_URL}?email=${email}`, data);
    return response.data;
};

export const createProfile = async (email, data) => {
    const response = await axios.post(`${API_URL}/create?email=${email}`, data);
    return response.data;
};