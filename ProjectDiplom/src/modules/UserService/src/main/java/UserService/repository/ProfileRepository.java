package UserService.repository;


import UserService.domain.model.UserProfile;
import UserService.domain.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(Users user);  // ✅ Должен возвращать Optional
}
