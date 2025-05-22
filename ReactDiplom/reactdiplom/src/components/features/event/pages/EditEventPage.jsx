
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { eventAPI } from '../services/eventService';
import EventForm from './EventForm';
import '../styles/EditEventPage.css';

const EditEventPage = () => {
    const { eventId } = useParams();
    const navigate = useNavigate();
    const [event, setEvent] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const loadEvent = async () => {
            try {
                const response = await eventAPI.getEventDetails(eventId);
                setEvent(response.data);
            } catch (error) {
                console.error('Ошибка загрузки:', error);
                setError('Не удалось загрузить данные мероприятия');
            } finally {
                setLoading(false);
            }
        };
        loadEvent();
    }, [eventId]);

    const handleSubmitSuccess = (updatedEvent) => {
        navigate(`/events/${updatedEvent.id}`, {
            state: { message: 'Мероприятие успешно обновлено' }
        });
    };

    const handleCancel = () => {
        navigate(-1); // Возврат на предыдущую страницу
    };

    if (loading) {
        return <div className="loading-container">Загрузка данных мероприятия...</div>;
    }

    if (error) {
        return (
            <div className="error-container">
                <h2>Ошибка!</h2>
                <p>{error}</p>
                <button onClick={() => navigate('/my-events')} className="return-button">
                    Вернуться к списку мероприятий
                </button>
            </div>
        );
    }

    return (
        <div className="edit-event-page">
            <div className="page-header">
                <h1>Редактирование мероприятия</h1>
                <button onClick={handleCancel} className="cancel-button">
                    Отменить изменения
                </button>
            </div>

            <div className="form-wrapper">
                <EventForm
                    initialData={event}
                    onSuccess={handleSubmitSuccess}
                    onCancel={handleCancel}
                />
            </div>
        </div>
    );
};

export default EditEventPage;