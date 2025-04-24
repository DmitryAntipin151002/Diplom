package UserService.repository;

import UserService.model.UserPhoto;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @EntityGraph(attributePaths = {"user"})
    List<UserPhoto> findByUserId(UUID userId);

    @Modifying
    @Query("DELETE FROM UserPhoto p WHERE p.user.id = :userId AND p.id = :photoId")
    int deleteByUserIdAndId(@Param("userId") UUID userId, @Param("photoId") UUID photoId);

    @EntityGraph(attributePaths = {"user"})
    Optional<UserPhoto> findByUserIdAndIsProfilePhotoTrue(UUID userId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM UserPhoto p WHERE p.user.id = :userId AND p.id = :photoId")
    boolean existsByUserIdAndId(@Param("userId") UUID userId, @Param("photoId") UUID photoId);

    @EntityGraph(attributePaths = {"user"})
    Optional<UserPhoto> findByUserIdAndId(UUID userId, UUID photoId);

    @Modifying
    @Query("UPDATE UserPhoto p SET p.isProfilePhoto = false WHERE p.user.id = :userId AND p.isProfilePhoto = true")
    void resetProfilePhoto(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE UserPhoto p SET p.isProfilePhoto = true WHERE p.id = :photoId AND p.user.id = :userId")
    int setAsProfilePhoto(@Param("userId") UUID userId, @Param("photoId") UUID photoId);

    @Query("SELECT p FROM UserPhoto p WHERE p.user.id = :userId AND p.photoUrl LIKE %:filePath%")
    Optional<UserPhoto> findByUserIdAndPhotoUrlContaining(@Param("userId") UUID userId,
                                                          @Param("filePath") String filePath);

    @Query("SELECT COUNT(p) FROM UserPhoto p WHERE p.user.id = :userId")
    long countByUserId(@Param("userId") UUID userId);
}