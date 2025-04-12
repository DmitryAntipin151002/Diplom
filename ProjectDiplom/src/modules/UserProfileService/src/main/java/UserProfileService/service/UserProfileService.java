package UserProfileService.service;


import UserProfileService.model.UserProfile;
import UserProfileService.model.UserPhoto;
import UserProfileService.model.UserActivity;
import UserProfileService.repository.UserProfileRepository;
import UserProfileService.repository.UserPhotoRepository;
import UserProfileService.repository.UserActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;

    // Создание профиля пользователя
    public UserProfile createProfile(UserProfile userProfile) {
        userProfile.setCreatedAt(java.time.LocalDateTime.now());
        userProfile.setUpdatedAt(userProfile.getCreatedAt());
        return userProfileRepository.save(userProfile);
    }

    // Получение профиля пользователя по ID
    public Optional<UserProfile> getProfileByUserId(UUID userId) {
        return userProfileRepository.findByUserId(userId);
    }

    // Редактирование профиля пользователя
    public UserProfile updateProfile(UUID userId, UserProfile updatedProfile) {
        UserProfile existingProfile = userProfileRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
        existingProfile.setBio(updatedProfile.getBio());
        existingProfile.setAvatarUrl(updatedProfile.getAvatarUrl());
        existingProfile.setLocation(updatedProfile.getLocation());
        existingProfile.setSportType(updatedProfile.getSportType());
        existingProfile.setFitnessLevel(updatedProfile.getFitnessLevel());
        existingProfile.setUpdatedAt(java.time.LocalDateTime.now());
        return userProfileRepository.save(existingProfile);
    }

    // Загрузка аватарки
    public UserPhoto uploadAvatar(UUID userId, String photoUrl) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setUserProfile(userProfile);
        userPhoto.setPhotoUrl(photoUrl);
        userPhoto.setIsMain(true);
        userPhoto.setUploadedAt(java.time.LocalDateTime.now());
        return userPhotoRepository.save(userPhoto);
    }

    // Добавление активности пользователя
    public UserActivity addActivity(UUID userId, UserActivity userActivity) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userActivity.setUserProfile(userProfile);
        userActivity.setActivityDate(java.time.LocalDateTime.now());
        return userActivityRepository.save(userActivity);
    }

    // Получение всех активностей пользователя
    public List<UserActivity> getUserActivities(UUID userId) {
        return userActivityRepository.findByUserProfile_UserId(userId);
    }
}

