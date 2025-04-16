package UserService.repository;



import UserService.model.User;
import UserService.model.UserProfile;
import UserService.model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

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
    Optional<UserStats> getUserStats(UUID userId);
    Optional<UserProfile> findByUser(User user);
}