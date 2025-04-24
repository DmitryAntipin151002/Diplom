import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { jwtDecode } from 'jwt-decode';
import {
    getProfile,
    updateProfile,
    getUserStats,
    setAvatarFromGallery
} from '../../services/ProfileService';
import {
    getUserPhotos,
    uploadPhotoFile,
    deletePhoto,
    setProfilePhoto
} from '../../services/userPhotos';
import ProfileForm from '../../components/Profile_Componemt/ProfileForm';
import AvatarUpload from '../../components/Profile_Componemt/AvatarUpload';
import UserStatsCard from '../../components/Profile_Componemt/UserStatsCard';
import PhotoGallery from '../../components/Profile_Componemt/PhotoGallery';
import LoadingSpinner from '../../components/Profile_Componemt/LoadingSpinner';
import './../../assets/ProfilePage.css';
import axios from "axios";

const ProfilePage = () => {
    const { userId: profileUserId } = useParams();
    const navigate = useNavigate();
    const [profile, setProfile] = useState(null);
    const [stats, setStats] = useState(null);
    const [photos, setPhotos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [editMode, setEditMode] = useState(false);
    const [error, setError] = useState('');
    const [currentUserId, setCurrentUserId] = useState(null);
    const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083';

    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                setLoading(true);
                const token = localStorage.getItem('authToken');

                if (token) {
                    const decoded = jwtDecode(token);
                    setCurrentUserId(decoded.userId);
                }

                const [profileResponse, statsResponse, photosResponse] = await Promise.all([
                    getProfile(profileUserId),
                    getUserStats(profileUserId),
                    getUserPhotos(profileUserId)
                ]);

                setProfile(profileResponse);
                setStats(statsResponse);
                setPhotos(photosResponse || []);
            } catch (err) {
                setError(err.message || 'Error loading profile');
                setProfile(null);
                setPhotos([]);
            } finally {
                setLoading(false);
            }
        };

        fetchProfileData();
    }, [profileUserId]);

    const handleAddFriend = async () => {
        try {
            if (!currentUserId) {
                navigate('/login');
                return;
            }

            if (currentUserId === profileUserId) {
                setError("Can't add yourself");
                return;
            }

            const token = localStorage.getItem('authToken');
            const response = await axios.post(
                `${API_BASE_URL}/api/relationships`,
                null,
                {
                    params: {
                        userId: currentUserId,
                        relatedUserId: profileUserId,
                        type: 'FRIEND'
                    },
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            if (response.status === 201) {
                alert('Friend request sent!');
                const updatedStats = await getUserStats(profileUserId);
                setStats(prev => ({
                    ...prev,
                    friendsCount: updatedStats.friendsCount + 1
                }));
            }
        } catch (err) {
            const errorMessage = err.response?.data?.message || 'Error sending request';
            setError(errorMessage);
            setTimeout(() => setError(''), 5000);
        }
    };

    const handleSaveProfile = async (updatedData) => {
        try {
            const response = await updateProfile(profileUserId, updatedData);
            setProfile(response);
            setEditMode(false);
        } catch (err) {
            setError(err.message || 'Error updating profile');
        }
    };

    const handleAvatarUpload = async (file) => {
        try {
            setLoading(true);
            const formData = new FormData();
            formData.append('file', file);

            const token = localStorage.getItem('authToken');
            await axios.post(
                `${API_BASE_URL}/api/users/${profileUserId}/avatar`,
                formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        Authorization: `Bearer ${token}`
                    },
                }
            );

            const updatedProfile = await getProfile(profileUserId);
            setProfile({
                ...updatedProfile,
                avatarUrl: `${API_BASE_URL}/api/files/${updatedProfile.avatarUrl}?t=${Date.now()}`
            });
        } catch (err) {
            setError(err.message || 'Error uploading avatar');
        } finally {
            setLoading(false);
        }
    };

    const handlePhotoUpload = async (file) => {
        try {
            const newPhoto = await uploadPhotoFile(profileUserId, file);
            setPhotos(prev => [...prev, newPhoto]);
        } catch (err) {
            setError('Failed to upload photo');
        }
    };

    const handleDeletePhoto = async (photoId) => {
        try {
            await deletePhoto(profileUserId, photoId);
            setPhotos(prev => prev.filter(p => p.id !== photoId));
            setError(''); // Сброс ошибок при успешном удалении
        } catch (err) {
            setError(err.message || 'Error deleting photo');
        }
    };

    const handleSetProfilePhoto = async (photoId) => {
        try {
            setLoading(true);
            await setProfilePhoto(profileUserId, photoId);
            const newAvatarUrl = await setAvatarFromGallery(profileUserId, photoId);

            setPhotos(prev => prev.map(p => ({
                ...p,
                isProfilePhoto: p.id === photoId
            })));

            setProfile(prev => ({
                ...prev,
                avatarUrl: newAvatarUrl
            }));
            setError(''); // Сброс ошибок при успешной установке
        } catch (err) {
            setError(err.message || 'Error setting profile photo');
        } finally {
            setLoading(false);
        }
    };
    const handleLogout = () => {
        localStorage.removeItem('authToken');
        navigate('/login');
    };

    const handleNavigate = (path) => {
        navigate(`/${path}/${profileUserId}`);
    };




    if (loading) return <LoadingSpinner />;

    return (
        <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ duration: 0.5 }}


            className="profile-page-container"
        >
            <div className="cyber-grid"></div>
            <div className="neon-line"></div>

            <div className="header-actions">
                <motion.button
                    className="logout-btn"
                    onClick={handleLogout}
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}

                >
                    Logout
                </motion.button>

                {currentUserId === profileUserId && (
                    <motion.button
                        className={`cyber-button ${editMode ? 'cancel-mode' : 'edit-mode'}`}
                        whileHover={{ scale: 1.05 }}
                        whileTap={{ scale: 0.95 }}
                        onClick={() => setEditMode(!editMode)}
                    >
        <span className="button-icon">
            {editMode ? '✕' : '✎'}
        </span>
                        <span className="button-text">
            {editMode ? 'Cancel' : 'Edit Profile'}
        </span>
                    </motion.button>
                )}
            </div>

            <AnimatePresence>
                {error && (
                    <motion.div
                        initial={{ y: -50, opacity: 0 }}
                        animate={{ y: 0, opacity: 1 }}
                        exit={{ y: -50, opacity: 0 }}
                        className="error-alert"
                    >
                        {error}
                        <button onClick={() => setError('')} className="close-alert">
                            <i className="icon-close"></i>
                        </button>
                    </motion.div>
                )}
            </AnimatePresence>

            {!profile ? (
                <div className="error-alert">Profile not found</div>
            ) : (
                <>
                    <div className="profile-header">
                        <motion.h1
                            initial={{ x: -20, opacity: 0 }}
                            animate={{ x: 0, opacity: 1 }}
                            transition={{ delay: 0.2 }}
                            className="neon-title"
                        >
              <span>
                <i className="icon-user"></i> {profile?.firstName || 'User'}
              </span>
                        </motion.h1>
                    </div>

                    <div className="profile-content">
                        <div className="main-panel">
                            <div className="avatar-section">
                                {editMode ? (
                                    <AvatarUpload
                                        currentAvatar={profile?.avatarUrl}
                                        onUpload={handleAvatarUpload}
                                    />
                                ) : (
                                    <motion.div
                                        initial={{ scale: 0.9, opacity: 0 }}
                                        animate={{ scale: 1, opacity: 1 }}
                                        className="avatar-preview"
                                    >
                                        <img
                                            src={profile.avatarUrl || `${API_BASE_URL}/default-avatar.png`}
                                            alt="User avatar"
                                        />
                                    </motion.div>
                                )}
                                {!editMode && currentUserId === profileUserId && (
                                    <motion.button
                                        className="edit-avatar-btn"
                                        onClick={() => setEditMode(true)}
                                        whileHover={{ scale: 1.05 }}
                                        whileTap={{ scale: 0.95 }}
                                    >
                                        <i className="icon-edit"></i> Change avatar
                                    </motion.button>
                                )}
                                <div className="violet-glow"></div>
                            </div>

                            <AnimatePresence mode="wait">
                                {editMode ? (
                                    <motion.div
                                        key="form"
                                        initial={{ opacity: 0 }}
                                        animate={{ opacity: 1 }}
                                        exit={{ opacity: 0 }}
                                        className="edit-form"
                                    >
                                        <ProfileForm
                                            profile={profile}
                                            onSave={handleSaveProfile}
                                            onCancel={() => setEditMode(false)}
                                        />
                                    </motion.div>
                                ) : (
                                    <motion.div
                                        key="view"
                                        initial={{ opacity: 0 }}
                                        animate={{ opacity: 1 }}
                                        exit={{ opacity: 0 }}
                                        className="info-section"
                                    >
                                        {currentUserId && currentUserId !== profileUserId && (
                                            <motion.button
                                                className="cyber-button friend-btn"
                                                onClick={handleAddFriend}
                                                whileHover={{ scale: 1.05 }}
                                            >
                                                <i className="icon-user-plus"></i> Add Friend
                                            </motion.button>
                                        )}

                                        <h2 className="neon-subtitle">
                                            {profile.firstName} {profile.lastName}
                                        </h2>
                                        <div className="bio-box">
                                            <i className="icon-quote"></i>
                                            <p>{profile.bio || 'No bio provided'}</p>
                                        </div>

                                        <div className="details-grid">
                                            <div className="detail-card">
                                                <i className="icon-sport"></i>
                                                <div>
                                                    <h3>Sport</h3>
                                                    <p>{profile.sportType || 'Not specified'}</p>
                                                </div>
                                            </div>
                                            <div className="detail-card">
                                                <i className="icon-globe"></i>
                                                <div>
                                                    <h3>Website</h3>
                                                    <a
                                                        href={profile.website || '#'}
                                                        target="_blank"
                                                        rel="noopener noreferrer"
                                                        className="cyber-link"
                                                    >
                                                        {profile.website || 'Not specified'}
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </motion.div>
                                )}
                            </AnimatePresence>
                        </div>

                        <div className="stats-panel">
                            {stats && (
                                <UserStatsCard
                                    stats={stats}
                                    onNotificationClick={() => handleNavigate('notifications')}
                                    onFriendsClick={() => handleNavigate('friends')}
                                    onEventsClick={() => handleNavigate('events')}
                                />
                            )}
                            <PhotoGallery
                                photos={photos}
                                onUpload={handlePhotoUpload}
                                onDelete={handleDeletePhoto}
                                onSetProfile={handleSetProfilePhoto}
                                isOwner={currentUserId === profileUserId}
                            />
                        </div>
                    </div>
                </>
            )}
        </motion.div>
    );
};

export default ProfilePage;