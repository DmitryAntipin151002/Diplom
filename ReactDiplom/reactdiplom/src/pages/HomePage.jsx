import React from 'react';
import { Link } from 'react-router-dom';

const HomePage = () => {
    return (
        <div className="min-h-screen bg-gray-100 flex flex-col items-center justify-center p-4">
            <div className="bg-white p-8 rounded-lg shadow-lg text-center max-w-2xl">
                <h1 className="text-4xl font-bold text-blue-600 mb-4">Welcome to UserService</h1>
                <p className="text-gray-700 mb-6">
                    UserService is your all-in-one platform for managing user profiles, friends, subscriptions, and activities.
                    Get started by logging in or registering a new account.
                </p>
                <div className="space-x-4">
                    <Link
                        to="/auth"
                        className="bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-600 transition duration-300"
                    >
                        Login
                    </Link>
                    <Link
                        to="/auth"
                        className="bg-green-500 text-white px-6 py-2 rounded-lg hover:bg-green-600 transition duration-300"
                    >
                        Register
                    </Link>
                </div>
            </div>

            <div className="mt-12 grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300">
                    <h2 className="text-xl font-semibold mb-2">Manage Profile</h2>
                    <p className="text-gray-700">Update your personal information and preferences.</p>
                    <Link
                        to="/profile"
                        className="mt-4 inline-block bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition duration-300"
                    >
                        Go to Profile
                    </Link>
                </div>

                <div className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300">
                    <h2 className="text-xl font-semibold mb-2">Friends</h2>
                    <p className="text-gray-700">Connect with friends and manage your relationships.</p>
                    <Link
                        to="/friends"
                        className="mt-4 inline-block bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition duration-300"
                    >
                        Go to Friends
                    </Link>
                </div>

                <div className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300">
                    <h2 className="text-xl font-semibold mb-2">Subscriptions</h2>
                    <p className="text-gray-700">Subscribe to your favorite sports and activities.</p>
                    <Link
                        to="/subscriptions"
                        className="mt-4 inline-block bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition duration-300"
                    >
                        Go to Subscriptions
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default HomePage;