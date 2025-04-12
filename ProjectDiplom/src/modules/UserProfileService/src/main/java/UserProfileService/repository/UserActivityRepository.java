package UserProfileService.repository;

import UserProfileService.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {
    // Метод для получения всех активностей пользователя по userId
    List<UserActivity> findByUserProfile_UserId(UUID userId);
}

