import React, { useEffect } from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);

// Этот код нужно добавить в компонент, который будет использовать логику с кнопками

const AppWithAnimations = () => {

    useEffect(() => {
        const handleSigninSignupToggle = () => {
            document.querySelector(".btn").addEventListener("click", function() {
                document.querySelector(".form-signin").classList.toggle("form-signin-left");
                document.querySelector(".form-signup").classList.toggle("form-signup-left");
                document.querySelector(".frame").classList.toggle("frame-long");
                document.querySelector(".signup-inactive").classList.toggle("signup-active");
                document.querySelector(".signin-active").classList.toggle("signin-inactive");
                document.querySelector(".forgot").classList.toggle("forgot-left");
                this.classList.remove("idle");
                this.classList.add("active");
            });
        };

        const handleSignupBtnToggle = () => {
            document.querySelector(".btn-signup").addEventListener("click", function() {
                document.querySelector(".nav").classList.toggle("nav-up");
                document.querySelector(".form-signup-left").classList.toggle("form-signup-down");
                document.querySelector(".success").classList.toggle("success-left");
                document.querySelector(".frame").classList.toggle("frame-short");
            });
        };

        const handleSigninBtnToggle = () => {
            document.querySelector(".btn-signin").addEventListener("click", function() {
                document.querySelector(".btn-animate").classList.toggle("btn-animate-grow");
                document.querySelector(".welcome").classList.toggle("welcome-left");
                document.querySelector(".cover-photo").classList.toggle("cover-photo-down");
                document.querySelector(".frame").classList.toggle("frame-short");
                document.querySelector(".profile-photo").classList.toggle("profile-photo-down");
                document.querySelector(".btn-goback").classList.toggle("btn-goback-up");
                document.querySelector(".forgot").classList.toggle("forgot-fade");
            });
        };

        handleSigninSignupToggle();
        handleSignupBtnToggle();
        handleSigninBtnToggle();

        return () => {
            // Clean up the event listeners on component unmount
            document.querySelector(".btn").removeEventListener("click", handleSigninSignupToggle);
            document.querySelector(".btn-signup").removeEventListener("click", handleSignupBtnToggle);
            document.querySelector(".btn-signin").removeEventListener("click", handleSigninBtnToggle);
        };

    }, []);

    return (
        <div className="container">
            <div className="frame">
                {/* Весь HTML код страницы, который ты привел ранее */}
            </div>
        </div>
    );
}

root.render(
    <React.StrictMode>
        <AppWithAnimations />
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
