package UserService.service.impl;

import UserService.dto.*;
import UserService.exception.ProfileNotFoundException;
import UserService.exception.UserNotFoundException;
import UserService.model.User;
import UserService.model.UserProfile;
import UserService.repository.UserProfileRepository;
import UserService.repository.UserRepository;
import UserService.service.FileStorageService;
import UserService.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final FileStorageService fileStorageService;
    private final ModelMapper modelMapper;

    @Override
    public ProfileDto getProfile(UUID userId) {
        UserProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));
        return modelMapper.map(profile, ProfileDto.class);
    }

    @Override
    @Transactional
    public ProfileDto updateProfile(UUID userId, ProfileUpdateDto updateDto) {
        UserProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        modelMapper.map(updateDto, profile);
        profile.setUpdatedAt(LocalDateTime.now());

        UserProfile updated = profileRepository.save(profile);
        return modelMapper.map(updated, ProfileDto.class);
    }

    @Override
    @Transactional
    public void updateAvatar(UUID userId, MultipartFile avatarFile) {
        if (avatarFile.isEmpty()) {
            throw new IllegalArgumentException("Avatar file cannot be empty");
        }

        UserProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        // Удаляем старый аватар, если он существует
        if (profile.getAvatarUrl() != null) {
            try {
                fileStorageService.delete(profile.getAvatarUrl());
            } catch (Exception e) {
                // Логируем ошибку, но не прерываем выполнение
                System.err.println("Failed to delete old avatar: " + e.getMessage());
            }
        }

        // Сохраняем новый аватар
        String newAvatarUrl = fileStorageService.store(avatarFile);
        profile.setAvatarUrl(newAvatarUrl);
        profile.setUpdatedAt(LocalDateTime.now());
        profileRepository.save(profile);
    }

    @Override
    public UserStatsDto getUserStats(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ProfileNotFoundException(userId);
        }

        return profileRepository.getUserStats(userId)
                .map(stats -> modelMapper.map(stats, UserStatsDto.class))
                .orElseThrow(() -> new ProfileNotFoundException(userId));
    }

    @Override
    @Transactional
    public ProfileDto getOrCreateProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        UserProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user);
                    newProfile.setCreatedAt(LocalDateTime.now());
                    return profileRepository.save(newProfile);
                });

        // Явный маппинг без использования ModelMapper
        ProfileDto dto = new ProfileDto();
        dto.setUserId(profile.getUser().getId());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setBio(profile.getBio());
        dto.setLocation(profile.getLocation());
        dto.setWebsite(profile.getWebsite());
        dto.setAvatarUrl(profile.getAvatarUrl());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());

        return dto;
    }
}