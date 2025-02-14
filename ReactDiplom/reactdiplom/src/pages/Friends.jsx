// src/pages/Friends.jsx
import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { fetchFriends } from "../api/api";

const Friends = () => {
    const { user } = useAuth();
    const [friends, setFriends] = useState([]);

    useEffect(() => {
        if (user) {
            fetchFriends(user.id).then((data) => setFriends(data));
        }
    }, [user]);

    if (!friends.length) return <div>Loading friends...</div>;

    return (
        <div>
            <h1>Your Friends</h1>
            <ul>
                {friends.map((friend) => (
                    <li key={friend.id}>{friend.name}</li>
                ))}
            </ul>
        </div>
    );
};

export default Friends;
