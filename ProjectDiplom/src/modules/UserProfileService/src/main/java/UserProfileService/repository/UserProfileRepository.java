package UserProfileService.repository;

import UserProfileService.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    List<UserProfile> findByLocationContainingIgnoreCase(String location);
    List<UserProfile> findBySportTypeContainingIgnoreCase(String sportType);
}

