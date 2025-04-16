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
    void updateAvatar(UUID userId, MultipartFile avatarFile);

    UserStatsDto getUserStats(UUID userId);
    ProfileDto getOrCreateProfile(UUID userId);
}