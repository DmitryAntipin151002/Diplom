import React from 'react';

const ConfirmPasswordInput = ({ value, setValue, setError }) => (
    <div className="input-group">
        <input
            type="password"
            placeholder=" "
            value={value}
            onChange={(e) => {
                setValue(e.target.value);
                setError('');
            }}
        />
        <label>Повторите пароль</label>
    </div>
);

export default ConfirmPasswordInput;
