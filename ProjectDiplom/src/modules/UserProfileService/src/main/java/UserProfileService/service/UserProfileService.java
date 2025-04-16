package UserProfileService.service;

import UserProfileService.dto.UserProfileDTO;
import UserProfileService.exception.ProfileNotFoundException;
import UserProfileService.model.UserProfile;
import UserProfileService.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public UserProfileDTO getProfile(UUID userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));
        return convertToDTO(profile);
    }

    @Transactional
    public UserProfileDTO updateProfile(UUID userId, UserProfileDTO dto) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        profile.setAvatarUrl(dto.getAvatarUrl());
        profile.setBio(dto.getBio());
        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setGender(dto.getGender());
        profile.setLocation(dto.getLocation());
        profile.setSportType(dto.getSportType());
        profile.setFitnessLevel(dto.getFitnessLevel());
        profile.setGoals(dto.getGoals());
        profile.setAchievements(dto.getAchievements());
        profile.setPersonalRecords(dto.getPersonalRecords());

        return convertToDTO(userProfileRepository.save(profile));
    }

    @Transactional(readOnly = true)
    public List<UserProfileDTO> searchUsers(String searchTerm) {
        return userProfileRepository.findByLocationContainingIgnoreCase(searchTerm).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserProfileDTO convertToDTO(UserProfile profile) {
        // Реализация маппинга с использованием ModelMapper или вручную
    }
}