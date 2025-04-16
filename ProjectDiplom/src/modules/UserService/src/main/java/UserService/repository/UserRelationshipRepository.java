package UserService.repository;

import UserService.model.RelationshipStatus;
import UserService.model.RelationshipType;
import UserService.model.UserRelationship;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRelationshipRepository {
    List<UUID> findFriendIds(UUID userId);
    int countMutualFriends(UUID userId1, UUID userId2);
    List<UserRelationship> findByUserIdAndType(UUID userId, RelationshipType type);
    Optional<UserRelationship> findByUserIdAndRelatedUserIdAndType(
            UUID userId, UUID relatedUserId, RelationshipType type);
    List<UserRelationship> findByRelatedUserIdAndTypeAndStatus(
            UUID relatedUserId, RelationshipType type, RelationshipStatus status);
    UserRelationship save(UserRelationship relationship);
    Optional<UserRelationship> findById(Long relationshipId);
    boolean existsById(Long relationshipId);
    void deleteById(Long relationshipId);
}