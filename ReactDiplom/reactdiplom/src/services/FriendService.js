import axios from 'axios';

const API_URL = 'http://localhost:8083/api/relationships';

const FriendService = {
    getFriends: async (userId) => {
        try {
            const response = await axios.get(`${API_URL}/user/${userId}`, {
                params: { type: 'FRIEND' },
                timeout: 10000,
                validateStatus: (status) => status >= 200 && status < 500
            });

            if (response.status >= 400) {
                throw new Error(response.data?.message || `HTTP Error ${response.status}`);
            }

            return response.data.filter(f => f.status === 'ACCEPTED');
        } catch (err) {
            if (err.code === 'ECONNABORTED') {
                throw new Error('Request timeout');
            }
            if (!err.response) {
                throw new Error('Server connection error');
            }
            throw err;
        }
    },

    getPendingRequests: async (userId) => {
        try {
            const response = await axios.get(`${API_URL}/user/${userId}/pending-requests`, {
                timeout: 10000,
                validateStatus: (status) => status >= 200 && status < 500
            });

            if (response.status >= 400) {
                throw new Error(response.data?.message || `HTTP Error ${response.status}`);
            }

            return response.data;
        } catch (err) {
            if (err.code === 'ECONNABORTED') {
                throw new Error('Request timeout');
            }
            if (!err.response) {
                throw new Error('Server connection error');
            }
            throw err;
        }
    },

    getBlockedUsers: async (userId) => {
        try {
            const response = await axios.get(`${API_URL}/user/${userId}`, {
                params: { type: 'BLOCK' },
                timeout: 10000,
                validateStatus: (status) => status >= 200 && status < 500
            });

            if (response.status >= 400) {
                throw new Error(response.data?.message || `HTTP Error ${response.status}`);
            }

            return response.data;
        } catch (err) {
            if (err.code === 'ECONNABORTED') {
                throw new Error('Request timeout');
            }
            if (!err.response) {
                throw new Error('Server connection error');
            }
            throw err;
        }
    },

    sendFriendRequest: async (userId, relatedUserId) => {
        try {
            const response = await axios.post(API_URL, null, {
                params: {
                    userId,
                    relatedUserId,
                    type: 'FRIEND'
                },
                timeout: 10000,
                validateStatus: (status) => status >= 200 && status < 500
            });

            if (response.status >= 400) {
                throw new Error(response.data?.message || `HTTP Error ${response.status}`);
            }

            return response.data;
        } catch (err) {
            if (err.code === 'ECONNABORTED') {
                throw new Error('Request timeout');
            }
            if (!err.response) {
                throw new Error('Server connection error');
            }
            throw err;
        }
    },

    respondToRequest: async (relationshipId, accept) => {
        try {
            const status = accept ? 'ACCEPTED' : 'DECLINED';
            const response = await axios.patch(`${API_URL}/${relationshipId}/status`, null, {
                params: { status },
                timeout: 10000,
                validateStatus: (status) => status >= 200 && status < 500
            });

            if (response.status >= 400) {
                throw new Error(response.data?.message || `HTTP Error ${response.status}`);
            }

            return response.data;
        } catch (err) {
            if (err.code === 'ECONNABORTED') {
                throw new Error('Request timeout');
            }
            if (!err.response) {
                throw new Error('Server connection error');
            }
            throw err;
        }
    },

    searchUsers: async (query) => {
        try {
            const response = await axios.get(`http://localhost:8083/api/profiles/search`, {
                params: { query },
                timeout: 10000,
                validateStatus: (status) => status >= 200 && status < 500

            });

            if (response.status >= 400) {
                throw new Error(response.data?.message || `HTTP Error ${response.status}`);
            }

            return response.data.map(user => ({
                userId: user.userId,
                firstName: user.firstName,
                lastName: user.lastName,
                avatarUrl: user.avatarUrl,
                sportType: user.sportType,
                location: user.location}))
        } catch (err) {
            if (err.code === 'ECONNABORTED') {
                throw new Error('Request timeout');
            }
            if (!err.response) {
                throw new Error('Server connection error');
            }
            throw err;
        }
    },

    removeFriend: async (relationshipId) => {
        try {
            const response = await axios.delete(`${API_URL}/${relationshipId}`, {
                timeout: 10000,
                validateStatus: (status) => status >= 200 && status < 500
            });

            if (response.status >= 400) {
                throw new Error(response.data?.message || `HTTP Error ${response.status}`);
            }

            return response.data;
        } catch (err) {
            if (err.code === 'ECONNABORTED') {
                throw new Error('Request timeout');
            }
            if (!err.response) {
                throw new Error('Server connection error');
            }
            throw err;
        }
    },

    blockUser: async (userId, relatedUserId) => {
        try {
            const response = await axios.post(API_URL, null, {
                params: {
                    userId,
                    relatedUserId,
                    type: 'BLOCK'
                },
                timeout: 10000,
                validateStatus: (status) => status >= 200 && status < 500
            });

            if (response.status >= 400) {
                throw new Error(response.data?.message || `HTTP Error ${response.status}`);
            }

            return response.data;
        } catch (err) {
            if (err.code === 'ECONNABORTED') {
                throw new Error('Request timeout');
            }
            if (!err.response) {
                throw new Error('Server connection error');
            }
            throw err;
        }
    },

    unblockUser: async (relationshipId) => {
        try {
            const response = await axios.delete(`${API_URL}/${relationshipId}`, {
                timeout: 10000,
                validateStatus: (status) => status >= 200 && status < 500
            });

            if (response.status >= 400) {
                throw new Error(response.data?.message || `HTTP Error ${response.status}`);
            }

            return response.data;
        } catch (err) {
            if (err.code === 'ECONNABORTED') {
                throw new Error('Request timeout');
            }
            if (!err.response) {
                throw new Error('Server connection error');
            }
            throw err;
        }
    }
};

export default FriendService;