// components/PhotoGallery/PhotoGallery.jsx
import React, { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import './../../assets/PhotoGallery.css';

const PhotoGallery = ({
                          photos = [],
                          onUpload,
                          onDelete,
                          onSetProfile
                      }) => {
    const [selectedPhoto, setSelectedPhoto] = useState(null);
    const [isUploading, setIsUploading] = useState(false);
    const [fileInputKey, setFileInputKey] = useState(Date.now());

    const handleFileUpload = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        try {
            setIsUploading(true);
            await onUpload(file);
            setFileInputKey(Date.now());
        } catch (error) {
            console.error('Upload error:', error);
        } finally {
            setIsUploading(false);
        }
    };

    const handleSetMainPhoto = async () => {
        if (selectedPhoto && !selectedPhoto.isProfilePhoto) {
            try {
                await onSetProfile(selectedPhoto.id);
                setSelectedPhoto(null);
            } catch (error) {
                console.error('Failed to set main photo:', error);
            }
        }
    };

    const handleDeletePhoto = async () => {
        if (selectedPhoto) {
            try {
                await onDelete(selectedPhoto.id);
                setSelectedPhoto(null);
            } catch (error) {
                console.error('Failed to delete photo:', error);
            }
        }
    };

    return (
        <div className="cyber-gallery">
            <div className="gallery-header">
                <h2 className="cyber-title">PHOTO GALLERY</h2>
            </div>

            {photos.length === 0 ? (
                <div className="empty-gallery-instruction">
                    <p>Нажмите "UPLOAD" чтобы добавить первое фото</p>
                </div>
            ) : (
                <div className="photos-grid">
                    {photos.map(photo => (
                        <PhotoItem
                            key={photo.id}
                            photo={photo}
                            onSelect={setSelectedPhoto}
                            isSelected={selectedPhoto?.id === photo.id}
                        />
                    ))}
                </div>
            )}

            <div className="gallery-controls">
                <label className="cyber-button upload-btn">
                    {isUploading ? (
                        <span className="glitch-text">UPLOADING...</span>
                    ) : (
                        <>
                            <span className="button-icon">↑</span>
                            <span className="button-text">UPLOAD</span>
                        </>
                    )}
                    <input
                        key={fileInputKey}
                        type="file"
                        accept="image/*"
                        onChange={handleFileUpload}
                        disabled={isUploading}
                        style={{ display: 'none' }}
                    />
                </label>
            </div>

            <PhotoModal
                photo={selectedPhoto}
                onClose={() => setSelectedPhoto(null)}
                onDelete={handleDeletePhoto}
                onSetMain={handleSetMainPhoto}
            />
        </div>
    );
};

const PhotoItem = ({ photo, onSelect, isSelected }) => (
    <motion.div
        className={`cyber-photo ${photo.isProfilePhoto ? 'profile-badge' : ''} ${isSelected ? 'selected' : ''}`}
        onClick={() => onSelect(photo)}
        whileHover={{ scale: 1.02 }}
        whileTap={{ scale: 0.98 }}
    >
        <div className="photo-frame">
            <div className="scanlines"></div>
            <img
                src={photo.photoUrl}
                alt={photo.description || 'User photo'}
                className="pixelated"
                onError={(e) => {
                    e.target.src = '/cyber-placeholder.png';
                }}
            />
        </div>

        {photo.description && (
            <div className="photo-label">
                <span>{photo.description}</span>
            </div>
        )}

        {photo.isProfilePhoto && (
            <div className="profile-badge-label">
                <span className="badge-icon">★</span> MAIN PHOTO
            </div>
        )}
    </motion.div>
);

const PhotoModal = ({ photo, onClose, onDelete, onSetMain }) => (
    <AnimatePresence>
        {photo && (
            <div className="cyber-modal" onClick={onClose}>
                <div className="modal-content" onClick={e => e.stopPropagation()}>
                    <div className="modal-header">
                        <h3>{photo.description || 'PHOTO DETAILS'}</h3>
                        <button className="cyber-button close" onClick={onClose}>
                            <span className="button-icon">✕</span>
                            <span className="button-text">CLOSE</span>
                        </button>
                    </div>

                    <div className="modal-image-container">
                        <div className="image-wrapper">
                            <img src={photo.photoUrl} alt="Full preview" />
                        </div>

                        <div className="modal-actions">
                            <motion.button
                                className={`cyber-button primary ${photo.isProfilePhoto ? 'disabled' : ''}`}
                                onClick={(e) => {
                                    e.stopPropagation();
                                    onSetMain();
                                }}
                                disabled={photo.isProfilePhoto}
                                whileHover={{ scale: 1.05 }}
                                whileTap={{ scale: 0.95 }}
                            >
                                <span className="button-icon">★</span>
                                <span className="button-text">SET MAIN</span>
                            </motion.button>

                            <motion.button
                                className="cyber-button danger"
                                onClick={(e) => {
                                    e.stopPropagation();
                                    onDelete();
                                }}
                                whileHover={{ scale: 1.05 }}
                                whileTap={{ scale: 0.95 }}
                            >
                                <span className="button-icon">🗑</span>
                                <span className="button-text">DELETE</span>
                            </motion.button>
                        </div>
                    </div>
                </div>
            </div>
        )}
    </AnimatePresence>
);

export default PhotoGallery;