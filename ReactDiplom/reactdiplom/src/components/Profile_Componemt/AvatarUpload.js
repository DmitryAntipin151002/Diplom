import React, { useState } from 'react';
import { motion } from 'framer-motion';
import './../../assets/AvatarUpload.css';

const AvatarUpload = ({ currentAvatar, onUpload }) => {
    const [preview, setPreview] = useState(null);
    const [uploading, setUploading] = useState(false);

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        // Создаем превью
        const reader = new FileReader();
        reader.onloadend = () => {
            setPreview(reader.result);
        };
        reader.readAsDataURL(file);

        // Загружаем файл
        setUploading(true);
        try {
            await onUpload(file);
        } finally {
            setUploading(false);
        }
    };

    return (
        <motion.div
            initial={{ scale: 0.9, opacity: 0 }}
            animate={{ scale: 1, opacity: 1 }}
            transition={{ delay: 0.3 }}
            className="avatar-upload-container"
        >
            <div className="avatar-preview-wrapper">
                <div className="avatar-preview">
                    <motion.img
                        src={preview || currentAvatar || '/default-avatar.png'}
                        alt="Аватар пользователя"
                        whileHover={{ scale: 1.03 }}
                        transition={{ type: "spring", stiffness: 400, damping: 10 }}
                    />
                    {uploading && (
                        <motion.div
                            initial={{ opacity: 0 }}
                            animate={{ opacity: 1 }}
                            className="uploading-overlay"
                        >
                            <div className="spinner"></div>
                        </motion.div>
                    )}
                </div>
                <motion.label
                    whileHover={{ scale: 1.1 }}
                    whileTap={{ scale: 0.95 }}
                    className="upload-indicator"
                    title="Изменить аватар"
                >
                    <input
                        type="file"
                        accept="image/*"
                        onChange={handleFileChange}
                        disabled={uploading}
                        style={{ display: 'none' }}
                    />
                    <svg viewBox="0 0 24 24">
                        <path
                            d="M12 15.5C13.933 15.5 15.5 13.933 15.5 12C15.5 10.067 13.933 8.5 12 8.5C10.067 8.5 8.5 10.067 8.5 12C8.5 13.933 10.067 15.5 12 15.5Z"
                            fill="currentColor"
                        />
                        <path
                            d="M20 4H16.8L15.2 2H8.8L7.2 4H4C2.9 4 2 4.9 2 6V18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM12 18C8.69 18 6 15.31 6 12C6 8.69 8.69 6 12 6C15.31 6 18 8.69 18 12C18 15.31 15.31 18 12 18Z"
                            fill="currentColor"
                        />
                    </svg>
                </motion.label>
            </div>

            <motion.label
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.98 }}
                className="upload-btn"
            >
                <input
                    type="file"
                    accept="image/*"
                    onChange={handleFileChange}
                    disabled={uploading}
                    style={{ display: 'none' }}
                />
                <span className="btn-content">
                    <i className="icon-upload"></i> Сменить аватар
                </span>
            </motion.label>
        </motion.div>
    );
};

export default AvatarUpload;