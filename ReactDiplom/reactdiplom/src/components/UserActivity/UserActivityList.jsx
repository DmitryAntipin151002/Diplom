import React, { useEffect, useState } from 'react';
import { getAllUserActivities } from '../../services/userActivityService';

const UserActivityList = () => {
    const [activities, setActivities] = useState([]);

    useEffect(() => {
        const fetchActivities = async () => {
            const data = await getAllUserActivities();
            setActivities(data);
        };
        fetchActivities();
    }, []);

    return (
        <div>
            {activities.map((activity) => (
                <div key={activity.id}>
                    <p>Events Created: {activity.eventsCreated}</p>
                    <p>Events Participated: {activity.eventsParticipated}</p>
                </div>
            ))}
        </div>
    );
};

export default UserActivityList;