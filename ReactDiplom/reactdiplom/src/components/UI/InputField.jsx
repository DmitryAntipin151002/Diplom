// src/components/UI/InputField.jsx
import React from "react";
import "./../features/auth/styles/LoginPage.css"; // если хочешь стили отдельно

const InputField = ({ label, type = "text", value, onChange, required = false }) => (
    <div className="input-group">
        <input
            type={type}
            placeholder=" "
            value={value}
            onChange={onChange}
            className="floating-input"
            required={required}
        />
        <label className="floating-label">{label}</label>
    </div>
);

export default InputField;
