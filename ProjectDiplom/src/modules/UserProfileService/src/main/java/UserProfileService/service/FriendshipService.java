package UserProfileService.service;

import UserProfileService.model.Friendship;
import UserProfileService.model.UserProfile;
import UserProfileService.repository.FriendshipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserProfileService userProfileService;

    @Transactional
    public FriendshipRequest sendFriendRequest(UUID userId, UUID friendId) {
        UserProfile user = userProfileService.getUserById(userId);
        UserProfile friend = userProfileService.getUserById(friendId);

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setFriend(friend);
        friendship.setStatus("PENDING");
        friendship.setCreatedAt(LocalDateTime.now());

        return friendshipRepository.save(friendship);
    }

    public Page<Friendship> getFriends(UUID userId, String status, Pageable pageable) {
        UserProfile user = userProfileService.getUserById(userId);
        return friendshipRepository.findByUserAndStatus(user, status, pageable);
    }
}