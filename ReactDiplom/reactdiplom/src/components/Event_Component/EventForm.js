import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import { eventAPI } from '../../services/eventService';

const EventForm = ({ onSuccess, initialData }) => {
    const [formData, setFormData] = useState(initialData || {
        title: '',
        description: '',
        sportType: 'FOOTBALL',
        startTime: new Date(),
        endTime: new Date(),
        location: '',
        statusCode: 'PLANNED'
    });

    const sportTypes = [
        { value: 'FOOTBALL', label: '⚽ Футбол' },
        { value: 'BASKETBALL', label: '🏀 Баскетбол' },
        { value: 'VOLLEYBALL', label: '🏐 Волейбол' },
        { value: 'TENNIS', label: '🎾 Теннис' }
    ];

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await eventAPI.createEvent(formData);
            onSuccess(response.data);
        } catch (error) {
            console.error('Ошибка создания события:', error);
        }
    };

    return (
        <form className="event-form" onSubmit={handleSubmit}>
            <div className="form-group">
                <label>Название события:</label>
                <input
                    type="text"
                    value={formData.title}
                    onChange={(e) => setFormData({...formData, title: e.target.value})}
                    required
                />
            </div>

            <div className="form-group">
                <label>Описание:</label>
                <textarea
                    value={formData.description}
                    onChange={(e) => setFormData({...formData, description: e.target.value})}
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
                        dateFormat="dd.MM.yyyy HH:mm"
                    />
                </div>

                <div className="form-group">
                    <label>Окончание:</label>
                    <DatePicker
                        selected={new Date(formData.endTime)}
                        onChange={(date) => setFormData({...formData, endTime: date})}
                        showTimeSelect
                        dateFormat="dd.MM.yyyy HH:mm"
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
                />
            </div>

            <button type="submit" className="submit-button">
                {initialData ? 'Обновить событие' : 'Создать событие'}
            </button>
        </form>
    );
};

export default EventForm;