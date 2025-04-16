// PhotoGallery.jsx
import React, { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { getUserPhotos, deletePhoto, setProfilePhoto, uploadPhoto } from '../../services/userPhotos';
import './../../assets/PhotoGallery.css';

const PhotoGallery = ({ userId }) => {
    const [photos, setPhotos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [selectedPhoto, setSelectedPhoto] = useState(null);
    const [showUpload, setShowUpload] = useState(false);

    useEffect(() => {
        const fetchPhotos = async () => {
            try {
                const response = await getUserPhotos(userId);
                setPhotos(response.data);
            } catch (err) {
                setError(err.response?.data?.message || 'Ошибка загрузки фотографий');
            } finally {
                setLoading(false);
            }
        };

        fetchPhotos();
    }, [userId]);

    const handleSetProfile = async (photoId) => {
        try {
            await setProfilePhoto(userId, photoId);
            const response = await getUserPhotos(userId);
            setPhotos(response.data);
        } catch (err) {
            setError(err.response?.data?.message || 'Ошибка обновления профильного фото');
        }
    };

    const handleDelete = async (photoId) => {
        try {
            await deletePhoto(userId, photoId);
            setPhotos(photos.filter(photo => photo.id !== photoId));
        } catch (err) {
            setError(err.response?.data?.message || 'Ошибка удаления фотографии');
        }
    };

    const handlePhotoUpload = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        try {
            setLoading(true);
            await uploadPhoto(userId, file);
            const response = await getUserPhotos(userId);
            setPhotos(response.data);
        } catch (err) {
            setError(err.response?.data?.message || 'Ошибка загрузки фотографии');
        } finally {
            setLoading(false);
            setShowUpload(false);
        }
    };

    if (loading) return (
        <div className="loading-photos">
            <div className="spinner"></div>
            <p>Загрузка фотографий...</p>
        </div>
    );

    return (
        <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.4 }}
            className="photo-gallery"
        >
            <div className="gallery-header">
                <h2>Фотогалерея</h2>
                <button
                    className="add-photo-btn"
                    onClick={() => setShowUpload(!showUpload)}
                >
                    <i className="icon-plus"></i> Добавить фото
                </button>
            </div>

            {showUpload && (
                <div className="upload-photo-container">
                    <label className="file-upload-label">
                        <input
                            type="file"
                            accept="image/*"
                            onChange={handlePhotoUpload}
                            style={{ display: 'none' }}
                        />
                        Выберите фото для загрузки
                    </label>
                </div>
            )}

            {error && (
                <motion.div
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    className="gallery-error"
                >
                    {error}
                </motion.div>
            )}

            {photos.length === 0 ? (
                <motion.p
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    className="no-photos"
                >
                    Нет загруженных фотографий
                </motion.p>
            ) : (
                <div className="photos-grid">
                    <AnimatePresence>
                        {photos.map(photo => (
                            <motion.div
                                key={photo.id}
                                layout
                                initial={{ scale: 0.9, opacity: 0 }}
                                animate={{ scale: 1, opacity: 1 }}
                                exit={{ scale: 0.9, opacity: 0 }}
                                transition={{ duration: 0.3 }}
                                className={`photo-item ${photo.isProfilePhoto ? 'profile-photo' : ''}`}
                                onClick={() => setSelectedPhoto(photo)}
                            >
                                <motion.img
                                    src={photo.photoUrl}
                                    alt={photo.description || 'Фото пользователя'}
                                    whileHover={{ scale: 1.03 }}
                                />
                                <div className="photo-actions">
                                    {!photo.isProfilePhoto && (
                                        <motion.button
                                            whileHover={{ scale: 1.05 }}
                                            whileTap={{ scale: 0.95 }}
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                handleSetProfile(photo.id);
                                            }}
                                            className="action-btn"
                                        >
                                            Сделать профильной
                                        </motion.button>
                                    )}
                                    <motion.button
                                        whileHover={{ scale: 1.05 }}
                                        whileTap={{ scale: 0.95 }}
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            handleDelete(photo.id);
                                        }}
                                        className="action-btn delete"
                                    >
                                        Удалить
                                    </motion.button>
                                </div>
                                {photo.description && (
                                    <p className="photo-description">{photo.description}</p>
                                )}
                            </motion.div>
                        ))}
                    </AnimatePresence>
                </div>
            )}

            <AnimatePresence>
                {selectedPhoto && (
                    <motion.div
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        exit={{ opacity: 0 }}
                        className="photo-modal"
                        onClick={() => setSelectedPhoto(null)}
                    >
                        <motion.div
                            className="modal-content"
                            initial={{ scale: 0.9 }}
                            animate={{ scale: 1 }}
                            onClick={e => e.stopPropagation()}
                        >
                            <img src={selectedPhoto.photoUrl} alt="Просмотр фото" />
                            <button
                                className="close-modal"
                                onClick={() => setSelectedPhoto(null)}
                            >
                                ×
                            </button>
                            {selectedPhoto.description && (
                                <p className="modal-description">{selectedPhoto.description}</p>
                            )}
                        </motion.div>
                    </motion.div>
                )}
            </AnimatePresence>
        </motion.div>
    );
};

export default PhotoGallery;