import axios from 'axios';

const API_URL = 'http://localhost:8080/api/user-activity';

export const getAllUserActivities = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const getUserActivityById = async (id) => {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
};

export const createUserActivity = async (data) => {
    const response = await axios.post(API_URL, data);
    return response.data;
};

export const updateUserActivity = async (id, data) => {
    const response = await axios.put(`${API_URL}/${id}`, data);
    return response.data;
};