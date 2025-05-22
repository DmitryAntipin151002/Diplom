import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/authentication-service';

const AuthService = {
    login: async (credentials) => {
        try {
            const response = await axios.post(
                `${API_URL}/login`,
                credentials,
                {
                    // 8. Добавляем таймаут запроса
                    timeout: 10000,
                    // 9. Настраиваем обработку статусов
                    validateStatus: (status) => status >= 200 && status < 500
                }
            );

            // 10. Проверяем успешный статус
            if (response.status >= 400) {
                throw new Error(response.data?.message || `HTTP Error ${response.status}`);
            }

            // 11. Проверяем структуру ответа
            if (!response.data.id || !response.data.preAuthorizationToken) {
                throw new Error('Некорректная структура ответа сервера');
            }

            return response;

        } catch (err) {
            // 12. Детальный анализ ошибок axios
            if (err.code === 'ECONNABORTED') {
                throw new Error('Таймаут соединения');
            }
            if (!err.response) {
                throw new Error('Нет связи с сервером');
            }
            throw err;
        }
    },
    register: (credentials) => {
        return axios.post(`${API_URL}/login/register`, credentials);
    },
    recoverPassword: (data) => {
        console.log('Отправка запроса восстановления:', JSON.stringify(data));
        return axios.post(`${API_URL}/login/recovery`, data)
            .then(response => {
                console.log('Успешный ответ:', response.data);
                return response.data;
            })
            .catch(error => {
                console.error('Ошибка запроса:', {
                    config: error.config,
                    response: error.response,
                    message: error.message
                });
                throw new Error(error.response?.data?.message || 'Ошибка соединения');
            });
    },

    updatePassword: (recoveryToken, passwordData) => {
        return axios.post(`${API_URL}/login/recovery/password`, passwordData, {
            headers: {
                Authorization: `Bearer ${recoveryToken}`
            }
        })
            .then(response => response.data)
            .catch(error => {
                throw new Error(error.response?.data?.message || 'Ошибка обновления пароля');
            });
    },

    verifyCode: (userId, code, tempToken) => {
        return axios.post(
            `${API_URL}/login/${userId}/code`, // Убрали /validate из URL
            { code: code }, // Код передаем в теле запроса
            {
                headers: {
                    Authorization: `Bearer ${tempToken}` // Используем preAuthorizationToken
                }
            }
        );
    },

    resendCode: (userId, tempToken) => {
        return axios.get(
            `${API_URL}/login/${userId}/code`,
            {
                headers: {
                    Authorization: `Bearer ${tempToken}`
                }
            }
        );
    }


};

export default AuthService;
