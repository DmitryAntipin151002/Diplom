package UserService.controller;

import UserService.dto.*;
import UserService.exception.UserNotFoundException;
import UserService.service.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

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

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<Void> updateAvatar(
            @PathVariable UUID userId,
            @RequestParam("avatar") MultipartFile avatarFile) {
        profileService.updateAvatar(userId, avatarFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/stats")
    public ResponseEntity<UserStatsDto> getUserStats(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getUserStats(userId));
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
}