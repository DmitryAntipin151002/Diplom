import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/authentication-service';

const AuthService = {
    login: (credentials) => {
        return axios.post(`${API_URL}/login`, credentials);
    },
    register: (credentials) => {
        return axios.post(`${API_URL}/login/register`, credentials);
    },
    recoverPassword: (email) => {
        return axios.post(`${API_URL}/recovery`, email);
    }
};

export default AuthService;
