import React from 'react';
import SubscriptionList from '../components/Subscriptions/SubscriptionList';
import SubscriptionForm from '../components/Subscriptions/SubscriptionForm';

const SubscriptionsPage = () => {
    return (
        <div className="container mx-auto p-4">
            <h1 className="text-2xl font-bold mb-4">Subscriptions</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <h2 className="text-xl font-semibold mb-4">Create Subscription</h2>
                    <SubscriptionForm />
                </div>
                <div className="bg-white p-6 rounded-lg shadow-md">
                    <h2 className="text-xl font-semibold mb-4">Subscription List</h2>
                    <SubscriptionList />
                </div>
            </div>
        </div>
    );
};

export default SubscriptionsPage;