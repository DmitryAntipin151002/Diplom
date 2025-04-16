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

const UserStatsCard = ({ stats }) => {
    return (
        <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.3 }}
            className="stats-card"
        >
            <h3 className="cyber-title">Статистика</h3>
            <div className="stats-grid">
                {[
                    { value: stats.eventCount || 0, label: 'Событий', color: '#7026e0' },
                    { value: stats.friendsCount || 0, label: 'Друзей', color: '#a239ff' },
                    { value: stats.notificationCount || 0, label: 'Уведомлений', color: '#5e2db3' }
                ].map((stat, i) => (
                    <motion.div
                        key={stat.label}
                        custom={i}
                        initial="hidden"
                        animate="visible"
                        variants={statVariants}
                        className="stat-item"
                        style={{ '--stat-color': stat.color }}
                    >
                        <div className="stat-glow"></div>
                        <div className="stat-value">{stat.value}</div>
                        <div className="stat-label">{stat.label}</div>
                    </motion.div>
                ))}
            </div>
        </motion.div>
    );
};

export default UserStatsCard;
