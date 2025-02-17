import React, { useEffect, useState } from 'react';
import { getAllSubscriptions } from '../../services/subscriptionService';
import Loader from '../Shared/Loader';

const SubscriptionList = () => {
    const [subscriptions, setSubscriptions] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchSubscriptions = async () => {
            try {
                const data = await getAllSubscriptions();
                setSubscriptions(data);
            } catch (error) {
                console.error('Error fetching subscriptions:', error);
            } finally {
                setLoading(false);
            }
        };
        fetchSubscriptions();
    }, []);

    if (loading) return <Loader />;

    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-xl font-semibold mb-4">Subscriptions</h2>
            {subscriptions.length > 0 ? (
                <ul className="space-y-2">
                    {subscriptions.map((subscription) => (
                        <li key={subscription.id} className="flex items-center space-x-4">
                            <span>{subscription.sportType}</span>
                            <span className="text-gray-500">{new Date(subscription.subscribedAt).toLocaleDateString()}</span>
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No subscriptions found.</p>
            )}
        </div>
    );
};

export default SubscriptionList;