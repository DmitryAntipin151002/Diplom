import React from 'react';

const SubmitButton = ({ isLoading }) => (
    <button type="submit" className="auth-button save-button" disabled={isLoading}>
        {isLoading ? (
            <div className="spinner"></div>
        ) : (
            <>
                <span>Сохранить изменения</span>
                <svg className="icon-arrow" viewBox="0 0 24 24">
                    <path d="M4 12h16m-7-7l7 7-7 7" />
                </svg>
            </>
        )}
    </button>
);

export default SubmitButton;
