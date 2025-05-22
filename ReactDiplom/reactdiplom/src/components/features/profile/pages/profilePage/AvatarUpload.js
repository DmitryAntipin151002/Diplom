
import React, { useState } from 'react';
import { motion } from 'framer-motion';

const AvatarUpload = ({ currentAvatar, onUpload }) => {
    const [preview, setPreview] = useState(currentAvatar);
    const [isUploading, setIsUploading] = useState(false);

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        // Create preview
        const reader = new FileReader();
        reader.onload = () => setPreview(reader.result);
        reader.readAsDataURL(file);

        try {
            setIsUploading(true);
            await onUpload(file);
        } catch (error) {
            console.error('Upload failed:', error);
        } finally {
            setIsUploading(false);
            e.target.value = ''; // Reset input
        }
    };

    return (
        <div className="avatar-upload">
            <div className="avatar-preview">
                <img
                    src={preview}
                    alt="Avatar preview"
                    className={isUploading ? 'uploading' : ''}
                />
                {isUploading && <div className="upload-spinner"></div>}
            </div>

            <label className="upload-label">
                {isUploading ? 'Uploading...' : 'Change Avatar'}
                <input
                    type="file"
                    accept="image/*"
                    onChange={handleFileChange}
                    disabled={isUploading}
                />
            </label>
        </div>
    );
};

export default AvatarUpload;