import React from 'react';
import PasswordHeader from './PasswordHeader';
import PasswordInput from './PasswordInput';
import ConfirmPasswordInput from './ConfirmPasswordInput';
import ErrorAlert from './../../../../UI/Alert';
import SubmitButton from './../../../../UI/SubmitButton';

const PasswordForm = ({
                          newPassword,
                          confirmPassword,
                          setNewPassword,
                          setConfirmPassword,
                          showPassword,
                          setShowPassword,
                          handleSubmit,
                          error,
                          setError,
                          isLoading,
                      }) => {
    return (
        <>
            <PasswordHeader />
            <form onSubmit={handleSubmit} className="auth-form">
                <PasswordInput
                    value={newPassword}
                    setValue={setNewPassword}
                    show={showPassword}
                    toggleShow={() => setShowPassword(!showPassword)}
                    setError={setError}
                />
                <ConfirmPasswordInput
                    value={confirmPassword}
                    setValue={setConfirmPassword}
                    setError={setError}
                />
                {error && <ErrorAlert message={error} clearError={() => setError('')} />}
                <SubmitButton isLoading={isLoading} />
            </form>
        </>
    );
};

export default PasswordForm;
