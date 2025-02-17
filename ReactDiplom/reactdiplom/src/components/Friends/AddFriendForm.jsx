import React, { useState } from 'react';
import { addFriend } from '../../services/friendService';

const AddFriendForm = ({ userId }) => {
    const [friendId, setFriendId] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        await addFriend(userId, { friendId });
        alert('Friend added successfully!');
        setFriendId('');
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <input
                type="text"
                placeholder="Friend ID"
                value={friendId}
                onChange={(e) => setFriendId(e.target.value)}
                className="w-full p-2 border rounded"
            />
            <button type="submit" className="w-full bg-blue-500 text-white p-2 rounded hover:bg-blue-600">
                Add Friend
            </button>
        </form>
    );
};

export default AddFriendForm;