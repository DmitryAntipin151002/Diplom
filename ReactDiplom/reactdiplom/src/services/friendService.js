import axios from 'axios';

const API_URL = 'http://localhost:8080/api/friends';

export const getFriends = async (userId) => {
    const response = await axios.get(`${API_URL}?userId=${userId}`);
    return response.data;
};

export const addFriend = async (userId, friendDTO) => {
    const response = await axios.post(`${API_URL}/add?userId=${userId}`, friendDTO);
    return response.data;
};

export const removeFriend = async (userId, friendId) => {
    await axios.delete(`${API_URL}/${friendId}?userId=${userId}`);
};