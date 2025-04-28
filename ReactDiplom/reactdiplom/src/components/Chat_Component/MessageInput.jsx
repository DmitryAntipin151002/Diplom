import React, { useState } from 'react';
import './../../assets/ChatStyles.css';

const MessageInput = ({ onSend }) => {
    const [message, setMessage] = useState('');
    const [attachments, setAttachments] = useState([]);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (message.trim() || attachments.length > 0) {
            onSend(message, attachments);
            setMessage('');
            setAttachments([]);
        }
    };

    const handleFileUpload = (e) => {
        const files = Array.from(e.target.files);
        setAttachments(prev => [...prev, ...files]);
    };

    return (
        <form className="message-input-form" onSubmit={handleSubmit}>
            <div className="input-container">
                <input
                    type="text"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder="Type a message..."
                    className="message-input"
                />

                <label className="file-upload-label">
                    <input
                        type="file"
                        multiple
                        onChange={handleFileUpload}
                        className="hidden-input"
                    />
                    📎
                </label>

                <button type="submit" className="send-button">
                    Send
                </button>
            </div>

            {attachments.length > 0 && (
                <div className="attachments-preview">
                    {attachments.map((file, index) => (
                        <span key={index} className="attachment-item">
                            {file.name}
                        </span>
                    ))}
                </div>
            )}
        </form>
    );
};

export default MessageInput;