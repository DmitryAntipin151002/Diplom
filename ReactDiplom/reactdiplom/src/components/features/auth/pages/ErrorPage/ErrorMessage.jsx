import React from 'react';

const ErrorMessage = ({ message }) => (
    <p className="subtitle">
        {message || 'Произошла непредвиденная ошибка'}
    </p>
);

export default ErrorMessage;
