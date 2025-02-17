import React, { useState } from 'react';
import { createSubscription } from '../../services/subscriptionService';

const SubscriptionForm = () => {
    const [sportType, setSportType] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        await createSubscription({ sportType });
        alert('Subscription created successfully!');
        setSportType('');
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-4">
            <input
                type="text"
                placeholder="Sport Type"
                value={sportType}
                onChange={(e) => setSportType(e.target.value)}
                className="w-full p-2 border rounded"
            />
            <button type="submit" className="w-full bg-blue-500 text-white p-2 rounded hover:bg-blue-600">
                Create Subscription
            </button>
        </form>
    );
};

export default SubscriptionForm;