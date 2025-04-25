import React, {useEffect, useRef, useState} from 'react';
import { motion } from 'framer-motion';
import './../../assets/ProfileForm.css';

const sportTypes = [
    'Футбол',
    'Баскетбол',
    'Теннис',
    'Плавание',
    'Бег',
    'Велоспорт',
    'Гимнастика',
    'Бокс',
    'Волейбол',
    'Хоккей',
    'Другое'
];

const ProfileForm = ({ profile, onSave, onCancel }) => {
    const [formData, setFormData] = useState({
        firstName: profile.firstName || '',
        lastName: profile.lastName || '',
        bio: profile.bio || '',
        location: profile.location || '',
        website: profile.website || '',
        sportType: profile.sportType || ''
    });

    const [isSportDropdownOpen, setIsSportDropdownOpen] = useState(false);
    const dropdownRef = useRef(null);

    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsSportDropdownOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSportSelect = (sport) => {
        setFormData(prev => ({ ...prev, sportType: sport }));
        setIsSportDropdownOpen(false);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSave(formData);
    };

    return (
        <motion.form
            initial={{ scale: 0.9, opacity: 0 }}
            animate={{ scale: 1, opacity: 1 }}
            transition={{ type: "spring", stiffness: 300, damping: 25 }}
            className="cyber-form"
            onSubmit={handleSubmit}
        >
            <div className="form-header">
                <h2 className="neon-heading">Редактирование профиля</h2>
                <button
                    type="button"
                    className="close-button"
                    onClick={onCancel}
                    aria-label="Закрыть"
                >
                    &times;
                </button>
            </div>

            <div className="form-grid">
                <div className="form-group">
                    <label className="input-label">
                        <i className="icon-user"></i> Имя
                    </label>
                    <input
                        type="text"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        className="cyber-input"
                        placeholder="Введите ваше имя"
                    />
                </div>

                <div className="form-group">
                    <label className="input-label">
                        <i className="icon-users"></i> Фамилия
                    </label>
                    <input
                        type="text"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                        className="cyber-input"
                        placeholder="Введите вашу фамилию"
                    />
                </div>

                <div className="form-group full-width">
                    <label className="input-label">
                        <i className="icon-edit"></i> О себе
                    </label>
                    <textarea
                        name="bio"
                        value={formData.bio}
                        onChange={handleChange}
                        className="cyber-textarea"
                        rows="4"
                        placeholder="Расскажите о себе..."
                    />
                </div>

                <div className="form-group">
                    <label className="input-label">
                        <i className="icon-map-pin"></i> Локация
                    </label>
                    <input
                        type="text"
                        name="location"
                        value={formData.location}
                        onChange={handleChange}
                        className="cyber-input"
                        placeholder="Город, страна"
                    />
                </div>

                <div className="form-group">
                    <label className="input-label">
                        <i className="icon-globe"></i> Вебсайт
                    </label>
                    <input
                        type="url"
                        name="website"
                        value={formData.website}
                        onChange={handleChange}
                        className="cyber-input"
                        placeholder="https://ваш-сайт.com"
                    />
                </div>

                <div className="form-group">
                    <label className="input-label">
                        <i className="icon-sport"></i> Вид спорта
                    </label>
                    <div className="cyber-dropdown" ref={dropdownRef}>
                        <div
                            className={`cyber-dropdown-toggle ${isSportDropdownOpen ? 'active' : ''}`}
                            onClick={() => setIsSportDropdownOpen(!isSportDropdownOpen)}
                        >
                            {formData.sportType || 'Выберите вид спорта'}
                            <i className={`icon-arrow ${isSportDropdownOpen ? 'up' : 'down'}`}></i>
                        </div>
                        {isSportDropdownOpen && (
                            <motion.div
                                className="cyber-dropdown-menu"
                                initial={{ opacity: 0, y: -10 }}
                                animate={{ opacity: 1, y: 0 }}
                                exit={{ opacity: 0, y: -10 }}
                                transition={{ duration: 0.2 }}
                            >
                                {sportTypes.map((sport) => (
                                    <div
                                        key={sport}
                                        className={`cyber-dropdown-item ${
                                            formData.sportType === sport ? 'active' : ''
                                        }`}
                                        onClick={() => handleSportSelect(sport)}
                                    >
                                        {sport}
                                    </div>
                                ))}
                            </motion.div>
                        )}
                    </div>
                </div>
            </div>

            <div className="form-actions">
                <motion.button
                    type="button"
                    className="cyber-button cancel"
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                    onClick={onCancel}
                >
                    Отмена
                </motion.button>
                <motion.button
                    type="submit"
                    className="cyber-button submit"
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                >
                    Сохранить изменения
                </motion.button>
            </div>
        </motion.form>
    );
};

export default ProfileForm;