import axios from 'axios';

const API_URL = 'http://your-backend-url/api/events';

export const createEvent = async (organizerId, eventData) => {
    return axios.post(API_URL, eventData, {
        headers: {
            'X-User-Id': organizerId
        }
    });
};

export const getEventsByOrganizer = async (organizerId) => {
    return axios.get(`${API_URL}/organizer/${organizerId}`);
};

export const getUpcomingEvents = async () => {
    return axios.get(`${API_URL}/upcoming`);
};

export const deleteEvent = async (eventId) => {
    return axios.delete(`${API_URL}/${eventId}`);
};