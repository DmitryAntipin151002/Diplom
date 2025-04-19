import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import './../../assets/PhotoGallery.css';

const PhotoGallery = ({ photos = [], onUpload, onDelete, onSetProfile }) => {
    const [selectedPhoto, setSelectedPhoto] = useState(null);
    const [isUploading, setIsUploading] = useState(false);

    const handleFileUpload = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        try {
            setIsUploading(true);
            await onUpload(file);
        } catch (error) {
            console.error('Upload error:', error);
        } finally {
            setIsUploading(false);
        }
    };

    return (
        <div className="photo-gallery">
            <div className="gallery-header">
                <h3>Фотогалерея</h3>
                <label className="upload-button">
                    {isUploading ? 'Загрузка...' : 'Добавить фото'}
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleFileUpload}
                        disabled={isUploading}
                        style={{ display: 'none' }}
                    />
                </label>
            </div>

            {photos.length === 0 ? (
                <p>Нет фотографий</p>
            ) : (
                <div className="photos-grid">
                    {photos.map(photo => (
                        <PhotoItem
                            key={photo.id}
                            photo={photo}
                            onSelect={setSelectedPhoto}
                            onDelete={onDelete}
                            onSetProfile={onSetProfile}
                        />
                    ))}
                </div>
            )}

            <PhotoModal
                photo={selectedPhoto}
                onClose={() => setSelectedPhoto(null)}
            />
        </div>
    );
};

const PhotoItem = ({ photo, onSelect, onDelete, onSetProfile }) => (
    <motion.div
        className={`photo-item ${photo.isProfilePhoto ? 'profile-photo' : ''}`}
        onClick={() => onSelect(photo)}
        whileHover={{ scale: 1.03 }}
    >
        <img
            src={photo.photoUrl}
            alt={photo.description || 'Фото'}
            onError={(e) => {
                e.target.src = '/placeholder.jpg';
            }}
        />
        <div className="photo-actions">
            {!photo.isProfilePhoto && (
                <button onClick={(e) => {
                    e.stopPropagation();
                    onSetProfile(photo.id);
                }}>
                    Сделать профильной
                </button>
            )}
            <button
                className="delete"
                onClick={(e) => {
                    e.stopPropagation();
                    onDelete(photo.id);
                }}
            >
                Удалить
            </button>
        </div>
    </motion.div>
);

const PhotoModal = ({ photo, onClose }) => (
    <AnimatePresence>
        {photo && (
            <div className="photo-modal" onClick={onClose}>
                <div className="modal-content" onClick={e => e.stopPropagation()}>
                    <img src={photo.photoUrl} alt="Просмотр" />
                    <button className="close-modal" onClick={onClose}>×</button>
                </div>
            </div>
        )}
    </AnimatePresence>
);

export default PhotoGallery;