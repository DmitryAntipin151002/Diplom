// src/components/UI/Alert.jsx
import React from "react";

const Alert = ({ type = "error", message, onClose }) => (
    <div className={`alert alert-${type} slide-in`}>
        {message}
        <button className="close-btn" onClick={onClose}>✖</button>
    </div>
);

const Alert2 = ({ type, message, onClose }) => {
    const alertClass = type === 'error' ? 'alert-error' : 'alert-success';

    return (
        <div className={`alert ${alertClass} slide-in`}>
            {message}
            <button className="close-btn" onClick={onClose}>✖</button>
        </div>
    );
};

const ErrorAlert = ({ message, clearError }) => (
    <div className="alert alert-error">
        ⚠️ {message}
        <button className="close-btn" onClick={clearError}>
            ✖
        </button>
    </div>
);



const ErrorAlert2 = ({ message, onClose }) => (
    <div className="error-alert">
        <span>{message}</span>
        <button onClick={onClose}>&times;</button>
    </div>
);



export default Alert;
