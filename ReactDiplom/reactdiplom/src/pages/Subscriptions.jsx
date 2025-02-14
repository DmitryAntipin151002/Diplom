// src/pages/Subscriptions.jsx
import React, { useEffect, useState } from "react";
import { fetchSubscriptions } from "../api/api";

const Subscriptions = () => {
    const [subscriptions, setSubscriptions] = useState([]);

    useEffect(() => {
        fetchSubscriptions().then((data) => setSubscriptions(data));
    }, []);

    if (!subscriptions.length) return <div>Loading subscriptions...</div>;

    return (
        <div>
            <h1>Your Subscriptions</h1>
            <ul>
                {subscriptions.map((subscription) => (
                    <li key={subscription.id}>{subscription.name}</li>
                ))}
            </ul>
        </div>
    );
};

export default Subscriptions;
