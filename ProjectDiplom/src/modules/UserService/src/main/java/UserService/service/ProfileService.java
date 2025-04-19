package UserService.service;


import UserService.dto.*;
import UserService.model.UserProfile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public interface ProfileService {
    ProfileDto getProfile(UUID userId);

    @Transactional
    ProfileDto updateProfile(UUID userId, ProfileUpdateDto updateDto);

    @Transactional
    void updateAvatarPath(UUID userId, String filePath);
    UserStatsDto getUserStats(UUID userId);
    ProfileDto getOrCreateProfile(UUID userId);
}