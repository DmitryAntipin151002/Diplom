import React from 'react';
import { Link } from 'react-router-dom';

const Navbar = () => {
    return (
        <nav className="bg-blue-500 p-4 text-white">
            <div className="container mx-auto flex justify-between items-center">
                <Link to="/" className="text-xl font-bold">UserService</Link>
                <div className="space-x-4">
                    <Link to="/auth" className="hover:text-blue-200">Auth</Link>
                    <Link to="/profile" className="hover:text-blue-200">Profile</Link>
                    <Link to="/friends" className="hover:text-blue-200">Friends</Link>
                    <Link to="/subscriptions" className="hover:text-blue-200">Subscriptions</Link>
                    <Link to="/activity" className="hover:text-blue-200">Activity</Link>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;