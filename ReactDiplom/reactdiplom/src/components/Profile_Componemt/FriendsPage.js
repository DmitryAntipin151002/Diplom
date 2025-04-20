import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import FriendService from '../../services/FriendService';
import '../../assets/FriendsPage.css';

const FriendsPage = () => {
    const { userId } = useParams();
    const [activeTab, setActiveTab] = useState('friends');
    const [friends, setFriends] = useState([]);
    const [pendingRequests, setPendingRequests] = useState([]);
    const [blockedUsers, setBlockedUsers] = useState([]);
    const [suggestions, setSuggestions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [searchTerm, setSearchTerm] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [isSearching, setIsSearching] = useState(false);


    useEffect(() => {
        const loadData = async () => {
            setLoading(true);
            setError('');
            try {
                switch (activeTab) {
                    case 'friends':
                        setFriends(await FriendService.getFriends(userId));
                        break;
                    case 'pending':
                        setPendingRequests(await FriendService.getPendingRequests(userId));
                        break;
                    case 'blocked':
                        setBlockedUsers(await FriendService.getBlockedUsers(userId));
                        break;
                    case 'suggestions':
                        setSuggestions(await FriendService.getSuggestions(userId));
                        break;
                    default: break;
                }
            } catch (err) {
                setError(err.message || 'Ошибка загрузки данных');
            } finally {
                setLoading(false);
            }
        };
        loadData();
    }, [activeTab, userId]);


    const handleSearch = async () => {
        if (!searchTerm.trim()) return;

        setIsSearching(true);
        try {
            const results = await FriendService.searchUsers(searchTerm);
            setSearchResults(results);
        } catch (err) {
            setError(err.message || 'Ошибка поиска');
        } finally {
            setIsSearching(false);
        }
    };




    const handleAcceptRequest = async (relationshipId) => {
        try {
            await FriendService.respondToRequest(relationshipId, true);
            const acceptedRequest = pendingRequests.find(r => r.id === relationshipId);
            setPendingRequests(prev => prev.filter(r => r.id !== relationshipId));
            setFriends(prev => [...prev, { ...acceptedRequest, status: 'ACCEPTED' }]);
        } catch (err) {
            setError(err.message || 'Ошибка принятия запроса');
        }
    };

    const handleDeclineRequest = async (relationshipId) => {
        try {
            await FriendService.respondToRequest(relationshipId, false);
            setPendingRequests(prev => prev.filter(r => r.id !== relationshipId));
        } catch (err) {
            setError(err.message || 'Ошибка отклонения запроса');
        }
    };

    const handleRemoveFriend = async (relationshipId) => {
        if (window.confirm('Вы уверены, что хотите удалить друга?')) {
            try {
                await FriendService.removeFriend(relationshipId);
                setFriends(prev => prev.filter(f => f.id !== relationshipId));
            } catch (err) {
                setError(err.message || 'Ошибка удаления друга');
            }
        }
    };

    const handleBlockUser = async (relatedUserId) => {
        if (window.confirm('Вы уверены, что хотите заблокировать пользователя?')) {
            try {
                await FriendService.blockUser(userId, relatedUserId);
                setFriends(prev => prev.filter(f => f.relatedUserId !== relatedUserId));
                setBlockedUsers(prev => [...prev, {
                    id: Date.now().toString(),
                    userId,
                    relatedUserId,
                    status: 'BLOCKED'
                }]);
            } catch (err) {
                setError(err.message || 'Ошибка блокировки пользователя');
            }
        }
    };

    const handleUnblockUser = async (relationshipId) => {
        try {
            await FriendService.unblockUser(relationshipId);
            setBlockedUsers(prev => prev.filter(b => b.id !== relationshipId));
        } catch (err) {
            setError(err.message || 'Ошибка разблокировки');
        }
    };

    const handleAddFriend = async (relatedUserId) => {
        try {
            const newRelationship = await FriendService.sendFriendRequest(userId, relatedUserId);

            setPendingRequests(prev => [...prev, {
                id: newRelationship.id,
                userId: newRelationship.userId,
                relatedUserId: newRelationship.relatedUserId,
                status: 'PENDING',
                user: newRelationship.user,
                relatedUser: newRelationship.relatedUser
            }]);

            setSearchResults(prev =>
                prev.filter(user => user.userId !== relatedUserId)
            );
        } catch (err) {
            setError(err.message || 'Ошибка отправки запроса');
        }
    };

    const handleOpenChat = (userId) => {
        alert(`Чат с пользователем User_${userId.substring(0,8)} пока недоступен`);
    };

    const filteredItems = (items) =>
        items.filter(item =>
            `user_${item.relatedUserId?.substring(0,8)}`
                .toLowerCase()
                .includes(searchTerm.toLowerCase())
        );

    const UserCard = ({ item, status }) => {
        const friendId = item?.relatedUserId || item?.userId;
        const firstName = item?.relatedUser?.profile?.firstName || item?.user?.profile?.firstName;
        const lastName = item?.relatedUser?.profile?.lastName || item?.user?.profile?.lastName;
        const locations = item?.relatedUser?.profile?.location || item?.user?.profile?.location;
        const sport_type = item?.relatedUser?.profile?.sportType || item?.user?.profile?.sportType;
        const user = item.user || { profile: {} };
        const relatedUser = item.relatedUser || { profile: {} };

        const displayUser = status === 'pending' ? relatedUser : user
        return (
            <div className="cyber-card">
                <img
                    src={item.relatedUser?.profile?.avatarUrl || `https://i.pravatar.cc/150?u=${friendId}`}
                    alt="Avatar"
                    className="user-avatar"
                />
                <div className="user-info">
                    <h3 className="user-name">
                        {firstName} {lastName}
                    </h3>
                    <div className="user-details">
                        <span className="icon-sport">🏆 {sport_type}</span>
                        <span>📍 {locations}</span>
                        {item.mutualFriendsCount > 0 && (
                            <span className="mutual-count">{item.mutualFriendsCount} общих друзей</span>
                        )}
                    </div>
                </div>

                <div className="user-actions">
                    {status === 'pending' && (
                        <>
                            <button className="cyber-button primary" onClick={() => handleAcceptRequest(item.id)}>
                                Принять
                            </button>
                            <button className="cyber-button danger" onClick={() => handleDeclineRequest(item.id)}>
                                Отклонить
                            </button>
                        </>
                    )}

                    {status === 'accepted' && (
                        <>
                            <button className="cyber-button secondary" onClick={() => handleOpenChat(friendId)}>
                                Чат
                            </button>
                            <button className="cyber-button danger" onClick={() => handleRemoveFriend(item.id)}>
                                Удалить
                            </button>
                            <button className="cyber-button secondary" onClick={() => handleBlockUser(friendId)}>
                                Блокировать
                            </button>
                        </>
                    )}

                    {status === 'suggestion' && (
                        <button className="cyber-button primary" onClick={() => handleAddFriend(item.userId)}>
                            Добавить
                        </button>
                    )}

                    {status === 'blocked' && (
                        <button className="cyber-button secondary" onClick={() => handleUnblockUser(item.id)}>
                            Разблокировать
                        </button>
                    )}
                </div>
            </div>
        );
    };

    const SearchResults = () => (
        <div className="search-results-section">
            <h3 className="neon-heading">Результаты поиска</h3>
            <div className="cyber-card-grid">
                {searchResults.map(user => (
                    <div key={user.userId} className="cyber-card">
                        <img
                            src={user.avatarUrl || `https://i.pravatar.cc/150?u=${user.userId}`}
                            alt="Avatar"
                            className="user-avatar"
                        />
                        <div className="user-info">
                            <h3 className="user-name">
                                {user.firstName} {user.lastName}
                            </h3>
                            <div className="user-details">
                                <span className="icon-sport">🏆 {user.sportType || 'Не указан'}</span>
                                <span>📍 {user.location || 'Не указано'}</span>
                            </div>
                        </div>
                        <div className="user-actions">
                            <button
                                className="cyber-button primary"
                                onClick={() => handleAddFriend(user.userId)}
                            >
                                Добавить в друзья
                            </button>
                        </div>
                    </div>
                ))}
                {searchResults.length === 0 && (
                    <div className="empty-state">Ничего не найдено</div>
                )}
            </div>
        </div>
    );

    return (
        <div className="cyber-container">
            <header className="cyber-header">
                <h1 className="neon-heading">СЕТЬ КОНТАКТОВ</h1>
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Поиск по имени или ID..."
                        className="cyber-input"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                    />
                    <button
                        className="cyber-button primary"
                        onClick={handleSearch}
                        disabled={!searchTerm.trim()}
                    >
                        Поиск
                    </button>
                </div>
            </header>

            {error && (
                <div className="alert alert-error">
                    {error}
                    <button className="close-btn" onClick={() => setError('')}>
                        &times;
                    </button>
                </div>
            )}

            {isSearching && <div className="loading">ПОИСК...</div>}

            {searchResults.length > 0 && <SearchResults />}

            <div className="cyber-tabs">
                {['friends', 'pending', 'suggestions', 'blocked'].map((tab) => (
                    <button
                        key={tab}
                        className={`cyber-tab ${activeTab === tab ? 'active' : ''}`}
                        onClick={() => setActiveTab(tab)}
                    >
                        {{
                            friends: `Друзья (${friends.length})`,
                            pending: `Запросы (${pendingRequests.length})`,
                            suggestions: `Рекомендации (${suggestions.length})`,
                            blocked: `Блокировки (${blockedUsers.length})`
                        }[tab]}
                    </button>
                ))}
            </div>

            <div className="cyber-tab-content">
                {loading ? (
                    <div className="loading">ЗАГРУЗКА ДАННЫХ...</div>
                ) : (
                    <div className="cyber-card-grid">
                        {activeTab === 'friends' && filteredItems(friends).map(item => (
                            <UserCard key={item.id} item={item} status="accepted" />
                        ))}

                        {activeTab === 'pending' && filteredItems(pendingRequests).map(item => (
                            <UserCard key={item.id} item={item} status="pending" />
                        ))}

                        {activeTab === 'suggestions' && suggestions.map(item => (
                            <UserCard key={item.id} item={item} status="suggestion" />
                        ))}

                        {activeTab === 'blocked' && filteredItems(blockedUsers).map(item => (
                            <UserCard key={item.id} item={item} status="blocked" />
                        ))}

                        {!loading && [
                            friends,
                            pendingRequests,
                            suggestions,
                            blockedUsers
                        ][['friends', 'pending', 'suggestions', 'blocked'].indexOf(activeTab)]?.length === 0 && (
                            <div className="empty-state">⛔ ДАННЫЕ ОТСУТСТВУЮТ</div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default FriendsPage;