package UserProfileService.repository;

import UserProfileService.model.Friendship;
import UserProfileService.model.UserProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {
    List<Friendship> findByUserAndStatus(UserProfile user, String status, Pageable pageable);
    Optional<Friendship> findByUserAndFriend(UserProfile user, UserProfile friend);
}
