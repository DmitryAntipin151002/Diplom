import axios from "axios";

const API_URL = "http://localhost:8181/api";

export const loginUser = async (credentials) => {
    const response = await axios.post(`${API_URL}/auth/login`, credentials);
    return response.data;
};

export const registerUser = async (data) => {
    const response = await axios.post(`${API_URL}/auth/register`, data);
    return response.data;
};

export const fetchProfile = async (email) => {
    const response = await axios.get(`${API_URL}/profile`, { params: { email } });
    return response.data;
};

export const fetchFriends = async (userId) => {
    const response = await axios.get(`${API_URL}/friends`, { params: { userId } });
    return response.data;
};

export const fetchSubscriptions = async () => {
    const response = await axios.get(`${API_URL}/subscriptions`);
    return response.data;
};