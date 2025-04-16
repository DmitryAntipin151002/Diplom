import React, { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { FiCamera, FiEdit, FiSave, FiX, FiActivity, FiAward, FiMapPin, FiUsers } from 'react-icons/fi';
import ProfileService from '../services/ProfileService';
import './../assets/ProfilePage.css';

const ProfilePage = () => {
    const { userId } = useParams();
    const navigate = useNavigate();
    const [state, setState] = useState({
        profile: null,
        activities: [],
        photos: [],
        loading: true,
        error: '',
        isEditing: false
    });
    const currentUser = localStorage.getItem('userId');

    const isValidUUID = (uuid) =>
        /^[0-9a-f]{8}-[0-9a-f]{4}-[4][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i.test(uuid);

    const fetchProfileData = useCallback(async () => {
        try {
            if (!isValidUUID(userId)) throw new Error('Invalid user ID');

            const [profileData, activitiesData, photosResponse] = await Promise.all([
                ProfileService.getProfile(userId),
                ProfileService.getUserActivities(userId),
                ProfileService.getUserPhotos(userId)
            ]);

            // Обработка данных фотографий
            const photosData = Array.isArray(photosResponse)
                ? photosResponse
                : photosResponse?.photos || [];

            setState(prev => ({
                ...prev,
                profile: {
                    ...profileData,
                    dateOfBirth: profileData.dateOfBirth ? new Date(profileData.dateOfBirth) : null,
                    lastActiveAt: new Date(profileData.lastActiveAt),
                    createdAt: new Date(profileData.createdAt),
                    updatedAt: new Date(profileData.updatedAt),
                    friends: profileData.friends || []
                },
                activities: activitiesData || [],
                photos: photosData,
                loading: false,
                error: ''
            }));
        } catch (err) {
            setState(prev => ({
                ...prev,
                loading: false,
                error: err.message
            }));
            setTimeout(() => navigate('/dashboard'), 3000);
        }
    }, [userId, navigate]);

    useEffect(() => {
        if (!isValidUUID(userId)) {
            navigate('/dashboard');
            return;
        }
        fetchProfileData();
    }, [userId, fetchProfileData, navigate]);

    const handleAvatarUpload = async (file) => {
        if (!file) return;

        try {
            const updatedProfile = await ProfileService.uploadAvatar(userId, file);
            setState(prev => ({
                ...prev,
                profile: { ...prev.profile, ...updatedProfile },
                photos: [{
                    id: Date.now(),
                    url: updatedProfile.avatarUrl,
                    isMain: true
                }, ...prev.photos]
            }));
        } catch (err) {
            setState(prev => ({ ...prev, error: 'Avatar upload failed: ' + err.message }));
        }
    };

    const handleProfileUpdate = async (formData) => {
        try {
            const updatedProfile = await ProfileService.updateProfile(userId, formData);
            setState(prev => ({
                ...prev,
                profile: updatedProfile,
                isEditing: false,
                error: ''
            }));
        } catch (err) {
            setState(prev => ({ ...prev, error: 'Save failed: ' + err.message }));
        }
    };

    if (state.loading) return <LoadingSpinner />;
    if (state.error) return <ErrorBanner message={state.error} />;

    return (
        <div className="profile-container">
            <ProfileHeader
                profile={state.profile}
                onAvatarUpload={handleAvatarUpload}
                isEditing={state.isEditing}
                setIsEditing={(value) => setState(prev => ({ ...prev, isEditing: value }))}
                currentUser={currentUser}
                userId={userId}
            />

            <div className="profile-content">
                <MainContent
                    profile={state.profile}
                    activities={state.activities}
                    isEditing={state.isEditing}
                    onSave={handleProfileUpdate}
                    setIsEditing={(value) => setState(prev => ({ ...prev, isEditing: value }))}
                />

                <Sidebar
                    photos={state.photos}
                    friends={state.profile.friends}
                />
            </div>
        </div>
    );
};

const LoadingSpinner = () => (
    <div className="loading-overlay">
        <div className="spinner"></div>
        <p>Loading profile...</p>
    </div>
);

const ErrorBanner = ({ message }) => (
    <div className="error-banner">
        <div className="error-content">
            ⚠️ {message}
        </div>
    </div>
);

const ProfileHeader = ({ profile, onAvatarUpload, isEditing, setIsEditing, currentUser, userId }) => (
    <header className="profile-header">
        <div className="avatar-section">
            <div className="avatar-wrapper">
                {profile?.avatarUrl ? (
                    <img
                        src={profile.avatarUrl}
                        alt="Avatar"
                        className="user-avatar"
                        onError={(e) => e.target.src = '/default-avatar.jpg'}
                    />
                ) : (
                    <div className="avatar-placeholder">
                        {profile?.username?.[0]?.toUpperCase() || 'U'}
                    </div>
                )}
                {currentUser === userId && (
                    <label className="avatar-upload-label">
                        <input
                            type="file"
                            accept="image/*"
                            onChange={(e) => onAvatarUpload(e.target.files[0])}
                        />
                        <FiCamera className="upload-icon" />
                    </label>
                )}
            </div>
            <div className="profile-info">
                <div className="username-section">
                    <h1 className="username">
                        {profile?.username || 'Unknown User'}
                        {profile?.isVerified && <span className="verified-badge">✓</span>}
                    </h1>
                    {currentUser === userId && (
                        <button
                            className={`edit-profile-btn ${isEditing ? 'cancel' : ''}`}
                            onClick={() => setIsEditing(!isEditing)}
                        >
                            {isEditing ? <FiX /> : <FiEdit />}
                            <span>{isEditing ? 'Cancel' : 'Edit Profile'}</span>
                        </button>
                    )}
                </div>
                <div className="stats-grid">
                    <StatCard
                        icon={<FiActivity />}
                        title="Activities"
                        value={profile?.totalActivities || 0}
                        color="#4a90e2"
                    />
                    <StatCard
                        icon={<FiAward />}
                        title="Wins"
                        value={profile?.totalWins || 0}
                        color="#50e3c2"
                    />
                    <StatCard
                        icon={<FiMapPin />}
                        title="Distance"
                        value={`${profile?.totalDistance || 0}km`}
                        color="#f5a623"
                    />
                    <StatCard
                        icon={<FiUsers />}
                        title="Friends"
                        value={profile?.friends?.length || 0}
                        color="#bd10e0"
                    />
                </div>
            </div>
        </div>
    </header>
);

const StatCard = ({ icon, title, value, color }) => (
    <div className="stat-card" style={{ borderLeft: `4px solid ${color}` }}>
        <div className="stat-icon">{icon}</div>
        <div className="stat-content">
            <div className="stat-value">{value}</div>
            <div className="stat-title">{title}</div>
        </div>
    </div>
);

const MainContent = ({ profile, activities, isEditing, onSave, setIsEditing }) => (
    <main className="main-content">
        <section className="profile-section">
            <h2 className="section-title">About</h2>
            {isEditing ? (
                <EditProfileForm profile={profile} onSave={onSave} setIsEditing={setIsEditing} />
            ) : (
                <ProfileInfo profile={profile} />
            )}
        </section>

        <section className="profile-section">
            <h2 className="section-title">Recent Activities</h2>
            <ActivitiesList activities={activities} />
        </section>
    </main>
);

const ProfileInfo = ({ profile }) => (
    <div className="bio-content">
        <InfoRow label="Birth Date" value={profile?.dateOfBirth?.toLocaleDateString()} />
        <InfoRow label="Gender" value={profile?.gender} />
        <InfoRow label="Location" value={profile?.location} />
        <InfoRow label="Sport Type" value={profile?.sportType} />
        <InfoRow label="Goals" value={profile?.goals} />
        <InfoRow label="Achievements" value={profile?.achievements} />
        <InfoRow label="Personal Records" value={profile?.personalRecords} />
    </div>
);

const InfoRow = ({ label, value }) => (
    <div className="bio-row">
        <span className="info-label">{label}</span>
        <span className="info-value">{value || 'Not specified'}</span>
    </div>
);

const EditProfileForm = ({ profile, onSave, setIsEditing }) => {
    const [formData, setFormData] = useState({
        username: profile?.username || '',
        dateOfBirth: profile?.dateOfBirth?.toISOString().split('T')[0] || '',
        gender: profile?.gender || '',
        location: profile?.location || '',
        sportType: profile?.sportType || '',
        goals: profile?.goals || '',
        achievements: profile?.achievements || '',
        personalRecords: profile?.personalRecords || ''
    });

    const handleSubmit = (e) => {
        e.preventDefault();
        onSave(formData);
    };

    return (
        <form className="edit-form" onSubmit={handleSubmit}>
            <div className="form-grid">
                <div className="form-group">
                    <label>Username</label>
                    <input
                        type="text"
                        value={formData.username}
                        onChange={(e) => setFormData(prev => ({ ...prev, username: e.target.value }))}
                    />
                </div>
                <div className="form-group">
                    <label>Birth Date</label>
                    <input
                        type="date"
                        value={formData.dateOfBirth}
                        onChange={(e) => setFormData(prev => ({ ...prev, dateOfBirth: e.target.value }))}
                    />
                </div>
                <div className="form-group">
                    <label>Gender</label>
                    <select
                        value={formData.gender}
                        onChange={(e) => setFormData(prev => ({ ...prev, gender: e.target.value }))}
                    >
                        <option value="">Select</option>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                    </select>
                </div>
                <div className="form-group">
                    <label>Location</label>
                    <input
                        type="text"
                        value={formData.location}
                        onChange={(e) => setFormData(prev => ({ ...prev, location: e.target.value }))}
                    />
                </div>
                <div className="form-group">
                    <label>Sport Type</label>
                    <input
                        type="text"
                        value={formData.sportType}
                        onChange={(e) => setFormData(prev => ({ ...prev, sportType: e.target.value }))}
                    />
                </div>
            </div>

            <div className="form-actions">
                <button type="button" className="cancel-btn" onClick={() => setIsEditing(false)}>
                    Cancel
                </button>
                <button type="submit" className="save-btn">
                    <FiSave /> Save Changes
                </button>
            </div>
        </form>
    );
};

const ActivitiesList = ({ activities }) => (
    <div className="activities-list">
        {activities.length > 0 ? (
            activities.map(activity => (
                <ActivityItem key={activity.id || activity.date} activity={activity} />
            ))
        ) : (
            <div className="empty-state">No activities yet</div>
        )}
    </div>
);

const ActivityItem = ({ activity }) => (
    <div className="activity-card">
        <div className="activity-icon">
            {activity.type === 'running' ? '🏃' : activity.type === 'cycling' ? '🚴' : '🏊'}
        </div>
        <div className="activity-info">
            <h3>{activity.title}</h3>
            <div className="activity-meta">
                <span>{new Date(activity.date).toLocaleDateString()}</span>
                <span>{activity.duration} mins</span>
                <span>{activity.distance} km</span>
            </div>
        </div>
    </div>
);

const Sidebar = ({ photos, friends }) => (
    <aside className="sidebar-content">
        <PhotoGallery photos={photos} />
        <FriendsList friends={friends} />
    </aside>
);

const PhotoGallery = ({ photos = [] }) => (
    <section className="profile-section">
        <h2 className="section-title">Gallery</h2>
        <div className="photo-grid">
            {photos.map((photo, index) => (
                <div key={photo.id || index} className="photo-item">
                    <img src={photo.url || photo.photoUrl} alt={photo.description} />
                    {photo.isMain && <div className="main-badge">Main</div>}
                </div>
            ))}
        </div>
    </section>
);

const FriendsList = ({ friends = [] }) => (
    <section className="profile-section">
        <h2 className="section-title">Friends</h2>
        <div className="friends-list">
            {friends.map(friend => (
                <FriendItem key={friend.id} friend={friend} />
            ))}
        </div>
    </section>
);

const FriendItem = ({ friend }) => (
    <Link to={`/profile/${friend.id}`} className="friend-item">
        <div className="friend-avatar">
            {friend.avatarUrl ? (
                <img src={friend.avatarUrl} alt={friend.username} />
            ) : (
                <div className="avatar-placeholder">
                    {friend.username?.[0]?.toUpperCase() || 'U'}
                </div>
            )}
        </div>
        <div className="friend-info">
            <span className="friend-name">{friend.username || 'Unknown User'}</span>
            <span className="friend-status">{friend.isOnline ? 'Online' : 'Offline'}</span>
        </div>
    </Link>
);

export default ProfilePage;