import React, { useState } from 'react';
import { motion } from 'framer-motion';

const AvatarUpload = ({ currentAvatar, onUpload }) => {
    const [preview, setPreview] = useState(null);

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (!file) return;

        if (!file.type.match('image.*')) {
            alert('Пожалуйста, выберите файл изображения');
            return;
        }

        const reader = new FileReader();
        reader.onloadend = () => {
            setPreview(reader.result);
        };
        reader.readAsDataURL(file);

        onUpload(file);
    };

    return (
        <div className="avatar-upload-container">
            <label>
                <input
                    type="file"
                    accept="image/*"
                    onChange={handleFileChange}
                    style={{ display: 'none' }}
                />
                <motion.div
                    className="avatar-preview"
                    whileHover={{ scale: 1.05 }}
                >
                    <img
                        src={preview || currentAvatar}
                        alt="Аватар"
                        onError={(e) => {
                            e.target.src = '/default-avatar.png';
                        }}
                    />
                    <div className="upload-overlay">
                        Выбрать фото
                    </div>
                </motion.div>
            </label>
        </div>
    );
};

export default AvatarUpload;