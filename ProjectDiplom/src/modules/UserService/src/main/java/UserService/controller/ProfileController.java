package UserService.controller;

import UserService.dto.*;
import UserService.exception.UserNotFoundException;
import UserService.model.UserPhoto;
import UserService.service.FileStorageService;
import UserService.service.ProfileService;
import UserService.service.UserPhotoService;
import UserService.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final FileStorageService fileStorageService;
    private final UserPhotoService userPhotoService;



    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ProfileDto> updateProfile(
            @PathVariable UUID userId,
            @RequestBody ProfileUpdateDto updateDto) {
        return ResponseEntity.ok(profileService.updateProfile(userId, updateDto));
    }

    @PostMapping(
            value = "/{userId}/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> updateAvatar(
            @PathVariable UUID userId,
            @RequestPart("avatar") MultipartFile avatarFile) {

        try {
            // Валидация файла
            if (avatarFile.isEmpty()) {
                throw new IllegalArgumentException("Avatar file cannot be empty");
            }

            if (!Objects.requireNonNull(avatarFile.getContentType()).startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }

            // Сохранение файла
            String filePath = fileStorageService.store(avatarFile, userId);

            // Обновление профиля
            profileService.updateAvatarPath(userId, filePath);

            return ResponseEntity.ok(filePath);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload avatar: " + e.getMessage()
            );
        }
    }

    /**
     * Явное создание профиля для пользователя (если еще не существует)
     * @param userId ID пользователя
     * @return созданный или существующий профиль
     */
    @PostMapping("/{userId}/create")
    @Transactional
    public ResponseEntity<ProfileDto> createProfileIfNotExists(@PathVariable UUID userId) {
        try {
            // Явно вызываем метод создания/получения профиля
            ProfileDto profile = profileService.getOrCreateProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/{photoId}/profile")
    public ResponseEntity<?> setAsProfilePhoto(
            @PathVariable UUID userId,
            @PathVariable UUID photoId) {

        if (photoId == null) {
            return ResponseEntity.badRequest().body("Photo ID is required");
        }

        try {
            UserPhoto photo = userPhotoService.setAsProfilePhoto(userId, photoId);
            return ResponseEntity.ok(photo);
        } catch (Exception e) {
        }
        return null;
    }


    @GetMapping("/{userId}/stats")
    public ResponseEntity<UserStatsDto> getUserStats(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getUserStats(userId));
    }
}