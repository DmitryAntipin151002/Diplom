import React from 'react';

const PasswordInput = ({ value, setValue, show, toggleShow, setError }) => (
    <div className="input-group password-input">
        <input
            type={show ? "text" : "password"}
            placeholder=" "
            value={value}
            onChange={(e) => {
                setValue(e.target.value);
                setError('');
            }}
        />
        <label>Новый пароль</label>
        <button type="button" className="toggle-password" onClick={toggleShow}>
            {show ? '👁️' : '👁️'}
        </button>
    </div>
);

export default PasswordInput;
