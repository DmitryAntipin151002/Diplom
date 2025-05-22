
import React from 'react';
import '../styles/ConfirmationModal.css'; // Создадим стили отдельно

const ConfirmationModal = ({ isOpen, onConfirm, onCancel, text }) => {
    if (!isOpen) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <div className="modal-body">
                    <p>{text}</p>
                </div>
                <div className="modal-actions">
                    <button
                        className="modal-button confirm-button"
                        onClick={onConfirm}
                    >
                        Подтвердить
                    </button>
                    <button
                        className="modal-button cancel-button"
                        onClick={onCancel}
                    >
                        Отмена
                    </button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmationModal;