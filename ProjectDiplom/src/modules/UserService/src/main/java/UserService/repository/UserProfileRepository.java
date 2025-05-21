package UserService.repository;



import UserService.model.User;
import UserService.model.UserProfile;
import UserService.model.UserStats;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID>, JpaSpecificationExecutor<UserProfile> {

    @Query("SELECT " +
            "COUNT(e) as eventCount, " +
            "COUNT(f) as friendsCount, " +
            "COUNT(n) as notificationCount " +
            "FROM User u " +
            "LEFT JOIN u.organizedEvents e " +
            "LEFT JOIN u.friends f " +
            "LEFT JOIN u.notifications n " +
            "WHERE u.id = :userId " +
            "GROUP BY u.id")
    Optional<UserStats> getUserStats(@Param("userId") UUID userId);

    @EntityGraph(attributePaths = {"user"})
    Optional<UserProfile> findByUser(User user);

    @Query("SELECT p FROM UserProfile p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<UserProfile> searchProfiles(@Param("query") String query, Pageable pageable);

    default List<UserProfile> searchProfiles(String query, int limit) {
        return searchProfiles(query, PageRequest.of(0, limit));
    }

    boolean existsByUserId(UUID userId);

    @Query("SELECT p.avatarUrl FROM UserProfile p WHERE p.user.id = :userId")
    Optional<String> findAvatarUrlByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE UserProfile p SET p.avatarUrl = :avatarUrl WHERE p.user.id = :userId")
    void updateAvatarUrl(@Param("userId") UUID userId, @Param("avatarUrl") String avatarUrl);

    @EntityGraph(attributePaths = {"user.photos"})
    Optional<UserProfile> findWithPhotosByUserId(UUID userId);
    Optional<UserProfile> findByUserId(UUID userId);


}
