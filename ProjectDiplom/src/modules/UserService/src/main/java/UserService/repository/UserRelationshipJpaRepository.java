package UserService.repository;

import UserService.model.RelationshipStatus;
import UserService.model.RelationshipType;
import UserService.model.UserRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRelationshipJpaRepository extends JpaRepository<UserRelationship, Long> {
    List<UserRelationship> findByUserIdAndType(UUID userId, RelationshipType type);
    Optional<UserRelationship> findByUserIdAndRelatedUserIdAndType(
            UUID userId, UUID relatedUserId, RelationshipType type);
    List<UserRelationship> findByRelatedUserIdAndTypeAndStatus(
            UUID relatedUserId, RelationshipType type, RelationshipStatus status);
    boolean existsById(Long relationshipId);


}