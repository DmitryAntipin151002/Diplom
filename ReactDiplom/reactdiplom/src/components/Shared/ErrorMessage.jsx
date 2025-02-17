import React from 'react';

const ErrorMessage = ({ message }) => {
    return (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-2 rounded">
            {message}
        </div>
    );
};

export default ErrorMessage;