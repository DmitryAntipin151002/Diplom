import React, { Component } from "react";
import { Link } from "react-router-dom";

class Navbar extends Component {
    render() {
        return (
            <nav>
                <ul>
                    <li><Link to="/">Главная</Link></li>
                    <li><Link to="/profile">Профиль</Link></li>
                    <li><Link to="/login">Войти</Link></li>
                </ul>
            </nav>
        );
    }
}

export default Navbar;
