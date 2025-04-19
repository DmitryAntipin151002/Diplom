package UserService.repository;

import UserService.model.RelationshipStatus;
import UserService.model.RelationshipType;
import UserService.model.RelationshipTypeEntity;
import UserService.model.UserRelationship;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRelationshipRepository {
    List<UUID> findFriendIds(UUID userId);
    int countMutualFriends(UUID userId1, UUID userId2);
    List<UserRelationship> findByUserIdAndType(UUID userId, RelationshipTypeEntity type);
    Optional<UserRelationship> findByUserIdAndRelatedUserIdAndType(
            UUID userId, UUID relatedUserId, RelationshipType type);
    List<UserRelationship> findByRelatedUserIdAndTypeAndStatus(
            UUID relatedUserId, RelationshipType type, RelationshipStatus status);
    UserRelationship save(UserRelationship relationship);
    Optional<UserRelationship> findById(Long relationshipId);
    boolean existsById(Long relationshipId);
    void deleteById(Long relationshipId);


    // Кастомный запрос для поиска отношений между пользователями
    @Query("SELECT ur FROM UserRelationship ur " +
            "WHERE ur.user.id = :userId " +
            "AND ur.relatedUser.id = :relatedUserId " +
            "AND ur.type.name = :typeName")
    Optional<UserRelationship> findByUsersAndType(
            @Param("userId") UUID userId,
            @Param("relatedUserId") UUID relatedUserId,
            @Param("typeName") String typeName
    );
}