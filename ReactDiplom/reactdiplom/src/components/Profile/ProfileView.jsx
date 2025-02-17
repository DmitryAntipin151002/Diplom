import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getProfile } from '../../services/profileService';
import Loader from '../Shared/Loader';

const ProfileView = () => {
    const { email } = useParams();
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const data = await getProfile(email);
                setProfile(data);
            } catch (error) {
                console.error('Error fetching profile:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchProfile();
    }, [email]);

    if (loading) return <Loader />;

    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            <h1 className="text-2xl font-bold mb-4">{profile.fullName}</h1>
            <img src={profile.photoUrl} alt="Profile" className="w-32 h-32 rounded-full mb-4" />
            <p className="text-gray-700">{profile.interests}</p>
        </div>
    );
};

export default ProfileView;