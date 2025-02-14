import React, { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import { fetchProfile } from "../api/api";

const Profile = () => {
    const { user } = useAuth();
    const [profile, setProfile] = useState(null);

    useEffect(() => {
        if (user) {
            fetchProfile(user.email).then((data) => setProfile(data));
        }
    }, [user]);

    if (!profile) return <div>Loading...</div>;

    return (
        <div>
            <h1>{profile.name}'s Profile</h1>
            <p>Email: {profile.email}</p>
            <p>Bio: {profile.bio}</p>
        </div>
    );
};

export default Profile;