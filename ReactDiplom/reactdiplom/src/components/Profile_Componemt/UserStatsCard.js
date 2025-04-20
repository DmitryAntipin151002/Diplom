import React from 'react';
import { motion } from 'framer-motion';
import './../../assets/UserStatsCard.css';

const statVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: (i) => ({
        opacity: 1,
        y: 0,
        transition: { delay: i * 0.1, duration: 0.5 }
    })
};
const UserStatsCard = ({ stats, onNotificationClick, onFriendsClick, onEventsClick }) => {
    return (
        <motion.div
            className="stats-card"
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.4 }}
        >
            <div className="stats-header">
                <h3 className="neon-subtitle">Статистика</h3>
            </div>

            <div className="stats-grid">
                <div className="stat-item">
                    <i className="icon-trophy"></i>
                    <span>{stats?.achievementsCount || 0}</span>
                    <small>Достижений</small>
                </div>

                <div className="stat-item clickable" onClick={onFriendsClick}>
                    <i className="icon-users"></i>
                    <span>{stats?.friendsCount || 0}</span>
                    <small>Друзей</small>
                </div>

                <div className="stat-item clickable" onClick={onEventsClick}>
                    <i className="icon-calendar"></i>
                    <span>{stats?.eventsCount || 0}</span>
                    <small>Событий</small>
                </div>
            </div>

            <div className="quick-actions">
                <motion.button
                    className="action-btn"
                    onClick={onNotificationClick}
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                >
                    <i className="icon-bell"></i> Уведомления
                </motion.button>
            </div>
        </motion.div>
    );
};


export default UserStatsCard;
