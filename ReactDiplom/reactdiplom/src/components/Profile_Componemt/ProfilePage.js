import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { getProfile, updateProfile, getUserStats, uploadAvatar } from '../../services/ProfileService';
import { getUserPhotos, uploadPhotoFile, deletePhoto, setProfilePhoto } from '../../services/userPhotos';
import ProfileForm from '../../components/Profile_Componemt/ProfileForm';
import AvatarUpload from '../../components/Profile_Componemt/AvatarUpload';
import UserStatsCard from '../../components/Profile_Componemt/UserStatsCard';
import PhotoGallery from '../../components/Profile_Componemt/PhotoGallery';
import LoadingSpinner from '../../components/Profile_Componemt/LoadingSpinner';
import './../../assets/ProfilePage.css';
import axios from "axios";

const ProfilePage = () => {
    const { userId } = useParams();
    const navigate = useNavigate();
    const [profile, setProfile] = useState(null);
    const [stats, setStats] = useState(null);
    const [photos, setPhotos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [editMode, setEditMode] = useState(false);
    const [error, setError] = useState('');
    const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8083';
    useEffect(() => {
        const fetchProfileData = async () => {
            try {
                setLoading(true);
                const [profileResponse, statsResponse, photosResponse] = await Promise.all([
                    getProfile(userId),
                    getUserStats(userId),
                    getUserPhotos(userId)
                ]);

                setProfile(profileResponse);
                setStats(statsResponse);
                setPhotos(photosResponse || []);
            } catch (err) {
                setError(err.message || 'Ошибка загрузки профиля');
                setProfile(null);
                setPhotos([]);
            } finally {
                setLoading(false);
            }
        };

        fetchProfileData();
    }, [userId]);

    const handleSaveProfile = async (updatedData) => {
        try {
            const response = await updateProfile(userId, updatedData);
            setProfile(response);
            setEditMode(false);
        } catch (err) {
            setError(err.message || 'Ошибка обновления профиля');
        }
    };

    const handleAvatarUpload = async (file) => {
        try {
            setLoading(true);
            const formData = new FormData();
            formData.append('file', file);

            const response = await axios.post(
                `${API_BASE_URL}/api/users/${userId}/avatar`,
                formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    },
                }
            );

            // Обновляем профиль с новым аватаром
            const updatedProfile = await getProfile(userId);
            setProfile({
                ...updatedProfile,
                avatarUrl: `${API_BASE_URL}/api/files/${updatedProfile.avatarUrl}`
            });
        } catch (err) {
            setError(err.message || 'Ошибка загрузки аватара');
        } finally {
            setLoading(false);
        }
    };

    const handlePhotoUpload = async (file) => {
        try {
            const formData = new FormData();
            formData.append('file', file);
            formData.append('description', 'Новое фото');

            const response = await axios.post(
                `http://localhost:8083/api/users/${userId}/photos/upload`,
                formData,
                {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                    },
                }
            );

            // Используем полный URL, который возвращает сервер
            const photoUrl = `http://localhost:8083${response.data.photoUrl}`;

            setPhotos(prev => [...prev, {
                id: response.data.id,
                photoUrl: photoUrl,
                description: response.data.description,
                isProfilePhoto: false
            }]);
        } catch (err) {
            console.error('Ошибка загрузки:', err);
            setError('Не удалось загрузить фото');
        }
    };

    const handleDeletePhoto = async (photoId) => {
        try {
            await deletePhoto(userId, photoId);
            setPhotos(prev => prev.filter(photo => photo.id !== photoId));
        } catch (err) {
            setError(err.message || 'Ошибка удаления фотографии');
        }
    };

    const handleSetProfilePhoto = async (photoId) => {
        try {
            setLoading(true);
            await setProfilePhoto(userId, photoId);

            // Обновляем список фото
            const updatedPhotos = await getUserPhotos(userId);
            setPhotos(updatedPhotos.map(photo => ({
                ...photo,
                photoUrl: photo.photoUrl.startsWith('/api/files/')
                    ? `${API_BASE_URL}${photo.photoUrl}`
                    : photo.photoUrl
            })));

            // Обновляем профиль для отображения нового аватара
            const updatedProfile = await getProfile(userId);
            setProfile({
                ...updatedProfile,
                avatarUrl: `${API_BASE_URL}/api/files/${updatedProfile.avatarUrl}`
            });
        } catch (err) {
            setError(err.message || 'Ошибка установки профильной фотографии');
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        navigate('/login');
    };

    const handleNavigate = (path) => {
        navigate(`/${path}`);
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
                    Выйти
                </motion.button>
                <motion.button
                    whileHover={{
                        scale: 1.05,
                        filter: 'brightness(1.2)'
                    }}
                    whileTap={{ scale: 0.95 }}
                    onClick={() => setEditMode(!editMode)}
                    className="cyber-button"
                >
                    {editMode ? (
                        <>
                            <i className="icon-close"></i> Отменить
                        </>
                    ) : (
                        <>
                            <i className="icon-edit"></i> Редактировать
                        </>
                    )}
                </motion.button>
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
                <div className="error-alert">Профиль не найден</div>
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
                                <i className="icon-user"></i> {profile?.firstName || 'Пользователь'}
                            </span>
                        </motion.h1>
                    </div>

                    <div className="profile-content">
                        <div className="main-panel">
                            <div className="avatar-section">
                                {editMode ? (
                                    <AvatarUpload
                                        currentAvatar={profile?.avatarUrl || '/default-avatar.png'}
                                        onUpload={handleAvatarUpload}
                                    />
                                ) : (
                                    <motion.div
                                        initial={{ scale: 0.9, opacity: 0 }}
                                        animate={{ scale: 1, opacity: 1 }}
                                        className="avatar-preview"
                                    >
                                        <img
                                            src={profile?.avatarUrl || '/default-avatar.png'}
                                            alt="Аватар пользователя"
                                        />
                                    </motion.div>
                                )}
                                {!editMode && (
                                    <motion.button
                                        className="edit-avatar-btn"
                                        onClick={() => setEditMode(true)}
                                        whileHover={{ scale: 1.05 }}
                                        whileTap={{ scale: 0.95 }}
                                    >
                                        <i className="icon-edit"></i> Изменить аватар
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
                                        <h2 className="neon-subtitle">
                                            {profile?.firstName} {profile?.lastName}
                                        </h2>
                                        <div className="bio-box">
                                            <i className="icon-quote"></i>
                                            <p>{profile?.bio || 'Нет информации о себе'}</p>
                                        </div>

                                        <div className="details-grid">
                                            <div className="detail-card">
                                                <i className="icon-sport"></i>
                                                <div>
                                                    <h3>Вид спорта</h3>
                                                    <p>{profile?.sportType || 'Не указан'}</p>
                                                </div>
                                            </div>
                                            <div className="detail-card">
                                                <i className="icon-globe"></i>
                                                <div>
                                                    <h3>Вебсайт</h3>
                                                    <a
                                                        href={profile?.website || '#'}
                                                        target="_blank"
                                                        rel="noopener noreferrer"
                                                        className="cyber-link"
                                                    >
                                                        {profile?.website || 'Не указан'}
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
                            />
                        </div>
                    </div>
                </>
            )}
        </motion.div>
    );
};

export default ProfilePage;