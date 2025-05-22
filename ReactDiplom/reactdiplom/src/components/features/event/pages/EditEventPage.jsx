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
                setLoading(true);
                const response = await eventAPI.getEventDetails(eventId);
                setEvent({
                    ...response.data,
                    startTime: new Date(response.data.startTime),
                    endTime: new Date(response.data.endTime)
                });
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
            state: {
                message: 'Мероприятие успешно обновлено',
                type: 'success'
            }
        });
    };

    const handleCancel = () => {
        if (window.confirm('Вы уверены, что хотите отменить изменения? Все несохраненные данные будут потеряны.')) {
            navigate(-1);
        }
    };

    if (loading) {
        return (
            <div className="edit-event-page">
                <div className="loading-container">
                    <div className="loading-spinner"></div>
                    <p>Загрузка данных мероприятия...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="edit-event-page">
                <div className="error-container">
                    <h2>Ошибка!</h2>
                    <p>{error}</p>
                    <div className="button-group">
                        <button
                            onClick={() => window.location.reload()}
                            className="return-button"
                        >
                            ⟳ Попробовать снова
                        </button>
                        <button
                            onClick={() => navigate('/my-events')}
                            className="return-button"
                        >
                            ← Вернуться к списку
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="edit-event-page">
            <div className="page-header">
                <h1>Редактирование мероприятия</h1>
                <button onClick={handleCancel} className="cancel-button">
                    ✖ Отменить изменения
                </button>
            </div>

            <div className="form-wrapper">
                {event && (
                    <EventForm
                        initialData={event}
                        onSuccess={handleSubmitSuccess}
                        onCancel={handleCancel}
                    />
                )}
            </div>
        </div>
    );
};

export default EditEventPage;