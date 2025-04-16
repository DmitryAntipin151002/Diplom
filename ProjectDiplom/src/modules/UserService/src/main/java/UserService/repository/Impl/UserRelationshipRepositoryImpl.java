package UserService.repository.Impl;

import UserService.model.RelationshipStatus;
import UserService.model.RelationshipType;
import UserService.model.UserRelationship;
import UserService.repository.UserRelationshipJpaRepository;
import UserService.repository.UserRelationshipRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRelationshipRepositoryImpl implements UserRelationshipRepository {
    private final UserRelationshipJpaRepository jpaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserRelationshipRepositoryImpl(UserRelationshipJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<UUID> findFriendIds(UUID userId) {
        return entityManager.createQuery(
                        "SELECT CASE WHEN r.user.id = :userId THEN r.relatedUser.id ELSE r.user.id END " +
                                "FROM UserRelationship r " +
                                "WHERE (r.user.id = :userId OR r.relatedUser.id = :userId) " +
                                "AND r.type = 'FRIEND' AND r.status = 'ACCEPTED'",
                        UUID.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public int countMutualFriends(UUID userId1, UUID userId2) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(r) FROM UserRelationship r " +
                                "WHERE r.type = 'FRIEND' AND r.status = 'ACCEPTED' " +
                                "AND ((r.user.id = :userId1 AND r.relatedUser.id IN (" +
                                "    SELECT r2.relatedUser.id FROM UserRelationship r2 " +
                                "    WHERE r2.user.id = :userId2 AND r2.type = 'FRIEND' AND r2.status = 'ACCEPTED'" +
                                ")) OR (r.user.id = :userId2 AND r.relatedUser.id IN (" +
                                "    SELECT r3.relatedUser.id FROM UserRelationship r3 " +
                                "    WHERE r3.user.id = :userId1 AND r3.type = 'FRIEND' AND r3.status = 'ACCEPTED'" +
                                ")))", Long.class)
                .setParameter("userId1", userId1)
                .setParameter("userId2", userId2)
                .getSingleResult();
        return count != null ? count.intValue() : 0;
    }

    @Override
    public List<UserRelationship> findByUserIdAndType(UUID userId, RelationshipType type) {
        return jpaRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public Optional<UserRelationship> findByUserIdAndRelatedUserIdAndType(
            UUID userId, UUID relatedUserId, RelationshipType type) {
        return jpaRepository.findByUserIdAndRelatedUserIdAndType(userId, relatedUserId, type);
    }

    @Override
    public List<UserRelationship> findByRelatedUserIdAndTypeAndStatus(
            UUID relatedUserId, RelationshipType type, RelationshipStatus status) {
        return jpaRepository.findByRelatedUserIdAndTypeAndStatus(relatedUserId, type, status);
    }

    @Override
    public UserRelationship save(UserRelationship relationship) {
        return jpaRepository.save(relationship);
    }

    @Override
    public Optional<UserRelationship> findById(Long relationshipId) {
        return jpaRepository.findById(relationshipId);
    }

    @Override
    public boolean existsById(Long relationshipId) {
        return jpaRepository.existsById(relationshipId);
    }

    @Override
    public void deleteById(Long relationshipId) {
        jpaRepository.deleteById(relationshipId);
    }
}