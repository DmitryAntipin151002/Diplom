import React, { useEffect, useState } from 'react';
import { getFriends } from '../../services/friendService';
import Loader from '../Shared/Loader';

const FriendList = ({ userId }) => {
    const [friends, setFriends] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchFriends = async () => {
            try {
                const data = await getFriends(userId);
                setFriends(data);
            } catch (error) {
                console.error('Error fetching friends:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchFriends();
    }, [userId]);

    if (loading) return <Loader />;

    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-xl font-semibold mb-4">Friends</h2>
            {friends.length > 0 ? (
                <ul className="space-y-2">
                    {friends.map((friend) => (
                        <li key={friend.friendId} className="flex items-center space-x-4">
                            <img src={friend.photoUrl} alt={friend.fullName} className="w-10 h-10 rounded-full" />
                            <span>{friend.fullName}</span>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No friends found.</p>
            )}
        </div>
    );
};

export default FriendList;