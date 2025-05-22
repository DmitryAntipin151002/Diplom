// components/EventForm.jsx
import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { eventAPI } from '../services/eventService';
import '../styles/EventForm.css';

const EventForm = ({ onSuccess, initialData, onCancel }) => {
    const [formData, setFormData] = useState(initialData || {
        title: '',
        description: '',
        sportType: 'FOOTBALL',
        startTime: new Date(),
        endTime: new Date(),
        location: '',
        statusCode: 'PLANNED'
    });

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const sportTypes = [
        { value: 'FOOTBALL', label: '⚽ Футбол' },
        { value: 'BASKETBALL', label: '🏀 Баскетбол' },
        { value: 'VOLLEYBALL', label: '🏐 Волейбол' },
        { value: 'TENNIS', label: '🎾 Теннис' }
    ];

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const organizerId = localStorage.getItem('userId');
            let response;

            if (initialData) {
                response = await eventAPI.updateEvent(initialData.id, formData);
            } else {
                response = await eventAPI.createEvent(organizerId, formData);
            }

            onSuccess(response.data);
        } catch (error) {
            console.error('Ошибка сохранения события:', error);
            setError('Не удалось сохранить событие');
        } finally {
            setLoading(false);
        }
    };

    return (
        <form className="event-form" onSubmit={handleSubmit}>
            <h2 className="form-title">{initialData ? 'Редактировать событие' : 'Создать новое событие'}</h2>

            {error && <div className="error-message">{error}</div>}

            <div className="form-group">
                <label>Название события:</label>
                <input
                    type="text"
                    value={formData.title}
                    onChange={(e) => setFormData({...formData, title: e.target.value})}
                    required
                    placeholder="Введите название события"
                />
            </div>

            <div className="form-group">
                <label>Описание:</label>
                <textarea
                    value={formData.description}
                    onChange={(e) => setFormData({...formData, description: e.target.value})}
                    placeholder="Опишите детали события"
                />
            </div>

            <div className="form-row">
                <div className="form-group">
                    <label>Вид спорта:</label>
                    <select
                        value={formData.sportType}
                        onChange={(e) => setFormData({...formData, sportType: e.target.value})}
                    >
                        {sportTypes.map((type) => (
                            <option key={type.value} value={type.value}>
                                {type.label}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label>Статус:</label>
                    <select
                        value={formData.statusCode}
                        onChange={(e) => setFormData({...formData, statusCode: e.target.value})}
                    >
                        <option value="PLANNED">Запланировано</option>
                        <option value="ONGOING">В процессе</option>
                        <option value="COMPLETED">Завершено</option>
                    </select>
                </div>
            </div>

            <div className="form-row">
                <div className="form-group">
                    <label>Начало:</label>
                    <DatePicker
                        selected={new Date(formData.startTime)}
                        onChange={(date) => setFormData({...formData, startTime: date})}
                        showTimeSelect
                        timeFormat="HH:mm"
                        timeIntervals={15}
                        dateFormat="dd.MM.yyyy HH:mm"
                        placeholderText="Выберите дату и время"
                    />
                </div>

                <div className="form-group">
                    <label>Окончание:</label>
                    <DatePicker
                        selected={new Date(formData.endTime)}
                        onChange={(date) => setFormData({...formData, endTime: date})}
                        showTimeSelect
                        timeFormat="HH:mm"
                        timeIntervals={15}
                        dateFormat="dd.MM.yyyy HH:mm"
                        placeholderText="Выберите дату и время"
                    />
                </div>
            </div>

            <div className="form-group">
                <label>Место проведения:</label>
                <input
                    type="text"
                    value={formData.location}
                    onChange={(e) => setFormData({...formData, location: e.target.value})}
                    required
                    placeholder="Укажите место проведения"
                />
            </div>

            <div className="form-actions">
                <button
                    type="button"
                    className="cancel-button"
                    onClick={onCancel}
                    disabled={loading}
                >
                    Отмена
                </button>
                <button
                    type="submit"
                    className="submit-button"
                    disabled={loading}
                >
                    {loading ? (
                        <span className="spinner"></span>
                    ) : initialData ? (
                        'Обновить событие'
                    ) : (
                        'Создать событие'
                    )}
                </button>
            </div>
        </form>
    );
};

export default EventForm;