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

import java.util.List;
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
            if (avatarFile.isEmpty()) {
                throw new IllegalArgumentException("Avatar file cannot be empty");
            }

            if (!Objects.requireNonNull(avatarFile.getContentType()).startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }

            // Сохранение файла
            String filePath = fileStorageService.store(avatarFile, userId);
            String avatarUrl = "/api/files/" + filePath;

            // Обновление профиля
            profileService.updateAvatarPath(userId, avatarUrl);

            return ResponseEntity.ok(avatarUrl);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to upload avatar: " + e.getMessage()
            );
        }
    }

    @PostMapping("/{userId}/create")
    @Transactional
    public ResponseEntity<ProfileDto> createProfileIfNotExists(@PathVariable UUID userId) {
        try {
            ProfileDto profile = profileService.getOrCreateProfile(userId);
            return ResponseEntity.ok(profile);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{userId}/avatar-from-gallery/{photoId}")
    public ResponseEntity<String> setAvatarFromGallery(
            @PathVariable UUID userId,
            @PathVariable UUID photoId) {
        try {
            // Получаем фото из галереи
            UserPhoto photo = userPhotoService.getPhoto(userId, photoId);

            // Обновляем аватар в профиле
            profileService.updateAvatarPath(userId, photo.getPhotoUrl());

            return ResponseEntity.ok(photo.getPhotoUrl());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Failed to set avatar from gallery: " + e.getMessage()
            );
        }
    }

    @GetMapping("/{userId}/stats")
    public ResponseEntity<UserStatsDto> getUserStats(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getUserStats(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProfileDto>> searchProfiles(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        List<ProfileDto> results = profileService.searchProfiles(query, limit);
        return ResponseEntity.ok(results);
    }
}