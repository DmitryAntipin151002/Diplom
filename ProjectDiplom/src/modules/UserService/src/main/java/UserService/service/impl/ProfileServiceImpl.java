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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public void updateAvatarPath(UUID userId, String filePath) {
        UserProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        profile.setAvatarUrl(filePath);
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

    @Override
    public List<ProfileDto> searchProfiles(String query, int limit) {
        Specification<UserProfile> spec = (root, criteriaQuery, cb) -> {
            String searchTerm = "%" + query.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), searchTerm),
                    cb.like(cb.lower(root.get("lastName")), searchTerm)
            );
        };

        return profileRepository.findAll(spec, PageRequest.of(0, limit))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProfileDto convertToDto(UserProfile profile) {
        return modelMapper.map(profile, ProfileDto.class);
    }
}