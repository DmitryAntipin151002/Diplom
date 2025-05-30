import React, { useState } from 'react';
import format from 'date-fns/format';
import { ru } from 'date-fns/locale';
import ConfirmationModal from './ConfirmationModal'; // Путь укажите корректный
import { editMessage, deleteMessage } from '../services/messageService';

const Message = ({ message, isCurrentUser, onEdit, onDelete }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [editedContent, setEditedContent] = useState(message.content);
    const [showActions, setShowActions] = useState(false);
    const [showDeleteModal, setShowDeleteModal] = useState(false);


    const handleEdit = async () => {
        try {
            await editMessage(message.id, editedContent);
            onEdit(message.id, editedContent);
            setIsEditing(false);
        } catch (error) {
            console.error('Ошибка редактирования:', error);
            alert('Не удалось изменить сообщение');
        }
    };


    const handleDeleteConfirm = async () => {
        try {

            const result = await deleteMessage(message.id);

            if (result?.status === 'DELETED') {
                onDelete(message.id, false, true); // Флаг успешного удаления
            }
        } catch (error) {
            console.error('Error details:', {
                message: error.message,
                stack: error.stack
            });
        }
        setShowDeleteModal(false);
    };

    const renderContent = () => {
        if (message.status === 'DELETED') {
            return <span className="deleted-message">Сообщение удалено</span>;
        }

        if (isEditing) {
            return (
                <div className="edit-message-container">
                    <textarea
                        value={editedContent}
                        onChange={(e) => setEditedContent(e.target.value)}
                        autoFocus
                    />
                    <div className="edit-actions">
                        <button onClick={handleEdit}>Сохранить</button>
                        <button onClick={() => setIsEditing(false)}>Отмена</button>
                    </div>
                </div>
            );
        }

        return <div className="message-content">{message.content}</div>;
    };

    const renderAttachments = () => {
        if (!message.attachments || message.attachments.length === 0) return null;

        return (
            <div className="message-attachments">
                {message.attachments.map((attachment) => (
                    <div key={attachment.fileUrl} className="attachment">
                        {attachment.fileType.startsWith('image/') ? (
                            <img
                                src={attachment.fileUrl}
                                alt={attachment.fileName}
                                className="attachment-image"
                            />
                        ) : (
                            <a
                                href={attachment.fileUrl}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="attachment-file"
                            >
                                {attachment.fileName}
                            </a>
                        )}
                    </div>
                ))}
            </div>
        );
    };

    const renderReply = () => {
        if (!message.replyToId) return null;

        // В реальном приложении здесь нужно получить сообщение по replyToId
        return (
            <div className="message-reply">
                <span className="reply-label">Ответ на:</span>
                <div className="reply-content">
                    {message.replyToContent || 'Сообщение'}
                </div>
            </div>
        );
    };

    return (
        <div
            className={`message ${isCurrentUser ? 'current-user' : 'other-user'}`}
            onMouseEnter={() => setShowActions(true)}
            onMouseLeave={() => setShowActions(false)}

        >
            <ConfirmationModal
                isOpen={showDeleteModal}
                onCancel={() => setShowDeleteModal(false)}
                onConfirm={handleDeleteConfirm}
                text="Вы уверены, что хотите удалить это сообщение?"
            />
        >
            {renderReply()}
            {renderContent()}
            {renderAttachments()}

            <div className="message-meta">
                <span className="message-time">
                    {format(new Date(message.sentAt), 'HH:mm', { locale: ru })}
                </span>

                {message.status === 'EDITED' && (
                    <span className="message-status">(изменено)</span>
                )}

                {isCurrentUser && showActions && message.status !== 'DELETED' && (
                    <div className="message-actions">
                        <button onClick={() => setIsEditing(true)}>✏️</button>
                        <button onClick={() => setShowDeleteModal(true)}>🗑️</button> {/* Исправлено */}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Message;