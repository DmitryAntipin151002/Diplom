import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { updateProfile } from '../../services/profileService';

const ProfileEdit = () => {
    const { email } = useParams();
    const [formData, setFormData] = useState({
        fullName: '',
        photoUrl: '',
        interests: '',
    });

    const handleSubmit = async (e) => {
        e.preventDefault();
        await updateProfile(email, formData);
        alert('Profile updated successfully!');
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <input
                type="text"
                placeholder="Full Name"
                value={formData.fullName}
                onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                className="w-full p-2 border rounded"
            />
            <input
                type="text"
                placeholder="Photo URL"
                value={formData.photoUrl}
                onChange={(e) => setFormData({ ...formData, photoUrl: e.target.value })}
                className="w-full p-2 border rounded"
            />
            <input
                type="text"
                placeholder="Interests"
                value={formData.interests}
                onChange={(e) => setFormData({ ...formData, interests: e.target.value })}
                className="w-full p-2 border rounded"
            />
            <button type="submit" className="w-full bg-blue-500 text-white p-2 rounded hover:bg-blue-600">
                Update Profile
            </button>
        </form>
    );
};

export default ProfileEdit;