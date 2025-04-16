package UserProfileService.repository;



import UserProfileService.model.UserPhoto;
import UserProfileService.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, UUID> {

    // Находим все фото пользователя с определенным статусом "основное"
    List<UserPhoto> findByUserProfileAndIsMain(UserProfile userProfile, Boolean isMain);

    // Находим все фото конкретного пользователя
    List<UserPhoto> findByUserProfile_UserId(UUID userId);
}

