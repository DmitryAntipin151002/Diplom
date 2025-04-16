import React from 'react';
import { motion } from 'framer-motion';
import './../../assets/LoadingSpinner.css';

const LoadingSpinner = () => {
    return (
        <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="loading-container"
        >
            <div className="spinner">
                {[...Array(8)].map((_, i) => (
                    <motion.div
                        key={i}
                        animate={{
                            opacity: [0.3, 1, 0.3],
                            scale: [0.8, 1.2, 0.8]
                        }}
                        transition={{
                            duration: 1.2,
                            repeat: Infinity,
                            repeatDelay: 0,
                            delay: i * 0.1
                        }}
                        className="spinner-dot"
                        style={{
                            transform: `rotate(${i * 45}deg) translate(0, -20px)`
                        }}
                    />
                ))}
            </div>
            <motion.p
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 0.5 }}
            >
                Загрузка...
            </motion.p>
        </motion.div>
    );
};

export default LoadingSpinner;