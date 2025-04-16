package UserService.repository;

import UserService.model.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, UUID> {
    List<UserPhoto> findByUserId(UUID userId);

    @Modifying
    @Query("DELETE FROM UserPhoto p WHERE p.user.id = :userId AND p.id = :photoId")
    void deleteByUserIdAndId(@Param("userId") UUID userId, @Param("photoId") UUID photoId);

    Optional<UserPhoto> findByUserIdAndIsProfilePhotoTrue(UUID userId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM UserPhoto p WHERE p.user.id = :userId AND p.id = :photoId")
    boolean existsByUserIdAndId(@Param("userId") UUID userId, @Param("photoId") UUID photoId);
}