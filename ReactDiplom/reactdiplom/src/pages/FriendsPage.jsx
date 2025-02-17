import React, { useState } from 'react';
import FriendList from '../components/Friends/FriendList';
import AddFriendForm from '../components/Friends/AddFriendForm';

const FriendsPage = () => {
    const [userId, setUserId] = useState('');

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Friends</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <h2 className="text-xl font-semibold mb-4">Add Friend</h2>
                    <input
                        type="text"
                        placeholder="Your User ID"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        className="w-full p-2 border rounded mb-4"
                    />
                    <AddFriendForm userId={userId} />
                </div>
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <h2 className="text-xl font-semibold mb-4">Friend List</h2>
                    <FriendList userId={userId} />
                </div>
            </div>
        </div>
    );
};

export default FriendsPage;