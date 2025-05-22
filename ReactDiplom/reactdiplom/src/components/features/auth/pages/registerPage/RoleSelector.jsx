import React from "react";

const RoleSelector = ({ roleId, onRoleChange }) => {
    return (
        <div className="role-selector">
            <div className="role-options">
                <label className={`role-card ${roleId === 1 ? 'active' : ''}`}>
                    <input
                        type="radio"
                        name="role"
                        value={1}
                        checked={roleId === 1}
                        onChange={() => onRoleChange(1)}
                    />
                    <div className="role-content">
                        <span className="role-icon">👑</span>
                        <span className="role-title">Администратор</span>
                        <span className="role-desc">Полный доступ к системе</span>
                    </div>
                </label>

                <label className={`role-card ${roleId === 2 ? 'active' : ''}`}>
                    <input
                        type="radio"
                        name="role"
                        value={2}
                        checked={roleId === 2}
                        onChange={() => onRoleChange(2)}
                    />
                    <div className="role-content">
                        <span className="role-icon">👤</span>
                        <span className="role-title">Пользователь</span>
                        <span className="role-desc">Базовые возможности</span>
                    </div>
                </label>
            </div>
        </div>
    );
};

export default RoleSelector;
