import React from 'react';

const VerifyCodeForm = ({
                            code,
                            setCode,
                            error,
                            isLoading,
                            handleSubmit,
                            handleResendCode
                        }) => {
    return (
        <form onSubmit={handleSubmit}>
            <div className="code-input-group">
                <input
                    type="text"
                    inputMode="numeric"
                    pattern="[0-9]*"
                    placeholder="000000"
                    value={code}
                    onChange={(e) => {
                        const value = e.target.value.replace(/\D/g, '');
                        setCode(value.slice(0, 6));
                    }}
                    className="code-input"
                    autoFocus
                />
            </div>

            <button
                type="submit"
                className="verify-button"
                disabled={isLoading}
            >
                {isLoading ? (
                    <div className="spinner"></div>
                ) : (
                    'Подтвердить'
                )}
            </button>

            {error && <div className="error-message">{error}</div>}

            <div className="resend-section">
                <p>Не получили код?</p>
                <button
                    type="button"
                    onClick={handleResendCode}
                    className="resend-button"
                >
                    Отправить код повторно
                </button>
            </div>
        </form>
    );
};

export default VerifyCodeForm;
