import axios from 'axios';

const API_URL = 'http://localhost:8080/api/subscriptions';

export const getAllSubscriptions = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const getSubscriptionById = async (id) => {
    const response = await axios.get(`${API_URL}/${id}`);
    return response.data;
};

export const createSubscription = async (data) => {
    const response = await axios.post(API_URL, data);
    return response.data;
};

export const updateSubscription = async (id, data) => {
    const response = await axios.put(`${API_URL}/${id}`, data);
    return response.data;
};