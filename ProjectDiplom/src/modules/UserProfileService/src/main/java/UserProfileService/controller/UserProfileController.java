package UserProfileService.controller;

import UserProfileService.model.UserProfile;
import UserProfileService.model.UserPhoto;
import UserProfileService.model.UserActivity;
import UserProfileService.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "User Profile API", description = "Управление пользовательскими профилями, аватарками и активностями")
@RestController
@RequestMapping("/user-profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;
    @Operation(
            summary = "Создать новый профиль пользователя",
            description = "Создаёт новый профиль пользователя с подробностями"
    )
    @ApiResponse(responseCode = "200", description = "Профиль успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные профиля")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @PostMapping("/{userId}")
    public ResponseEntity<UserProfile> createProfile(
            @PathVariable UUID userId,
            @RequestBody UserProfile userProfile) {
        try {
            // Устанавливаем userId из пути в объект профиля
            userProfile.setUserId(userId);
            UserProfile createdProfile = userProfileService.createProfile(userProfile);
            return ResponseEntity.ok(createdProfile);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ошибка создания профиля: " + e.getMessage());
        }
    }


    @Operation(
            summary = "Получить профиль пользователя по UUID",
            description = "Возвращает профиль пользователя, если он существует"
    )
    @ApiResponse(responseCode = "200", description = "Профиль найден")
    @ApiResponse(responseCode = "404", description = "Профиль не найден")
    @ApiResponse(responseCode = "400", description = "Некорректный UUID")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getProfile(@Parameter(description = "UUID пользователя") @PathVariable UUID userId) {
        Optional<UserProfile> userProfile = userProfileService.getProfileByUserId(userId);
        return userProfile.map(ResponseEntity::ok).orElseGet(() -> {
            throw new EntityNotFoundException("Профиль с UUID " + userId + " не найден");
        });
    }

    @Operation(
            summary = "Обновить профиль пользователя",
            description = "Обновляет данные существующего профиля пользователя"
    )
    @ApiResponse(responseCode = "200", description = "Профиль успешно обновлён")
    @ApiResponse(responseCode = "400", description = "Некорректные данные профиля")
    @ApiResponse(responseCode = "404", description = "Профиль не найден")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @PutMapping("/{userId}")
    public ResponseEntity<UserProfile> updateProfile(
            @Parameter(description = "UUID пользователя") @PathVariable UUID userId,
            @RequestBody UserProfile updatedProfile) {
        try {
            UserProfile updatedUserProfile = userProfileService.updateProfile(userId, updatedProfile);
            return ResponseEntity.ok(updatedUserProfile);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ошибка обновления профиля: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Загрузить аватар пользователя",
            description = "Привязывает ссылку на фото как аватар к указанному пользователю"
    )
    @ApiResponse(responseCode = "200", description = "Аватар успешно загружен")
    @ApiResponse(responseCode = "400", description = "Неверный URL аватара или UUID")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @PostMapping("/{userId}/avatar")
    public ResponseEntity<UserPhoto> uploadAvatar(
            @Parameter(description = "UUID пользователя") @PathVariable UUID userId,
            @Parameter(description = "URL аватара") @RequestParam String photoUrl) {
        try {
            UserPhoto userPhoto = userProfileService.uploadAvatar(userId, photoUrl);
            return ResponseEntity.ok(userPhoto);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка загрузки аватара: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Добавить активность пользователю",
            description = "Добавляет активность (например, пробежку, тренировку) для пользователя"
    )
    @ApiResponse(responseCode = "200", description = "Активность успешно добавлена")
    @ApiResponse(responseCode = "400", description = "Некорректные данные активности")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @PostMapping("/{userId}/activities")
    public ResponseEntity<UserActivity> addActivity(
            @Parameter(description = "UUID пользователя") @PathVariable UUID userId,
            @RequestBody UserActivity userActivity) {
        try {
            UserActivity createdActivity = userProfileService.addActivity(userId, userActivity);
            return ResponseEntity.ok(createdActivity);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка добавления активности: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Получить список активностей пользователя",
            description = "Возвращает все активности, привязанные к указанному пользователю"
    )
    @ApiResponse(responseCode = "200", description = "Список активностей получен")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @ApiResponse(responseCode = "400", description = "Некорректный UUID")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @GetMapping("/{userId}/activities")
    public ResponseEntity<List<UserActivity>> getUserActivities(
            @Parameter(description = "UUID пользователя") @PathVariable UUID userId) {
        List<UserActivity> activities = userProfileService.getUserActivities(userId);
        return ResponseEntity.ok(activities);
    }
}
