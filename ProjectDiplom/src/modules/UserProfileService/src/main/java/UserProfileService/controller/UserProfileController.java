package UserProfileService.controller;



import UserProfileService.model.UserProfile;
import UserProfileService.model.UserPhoto;
import UserProfileService.model.UserActivity;
import UserProfileService.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user-profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    // Создание профиля
    @PostMapping
    public ResponseEntity<UserProfile> createProfile(@RequestBody UserProfile userProfile) {
        UserProfile createdProfile = userProfileService.createProfile(userProfile);
        return ResponseEntity.ok(createdProfile);
    }

    // Получение профиля по userId
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable UUID userId) {
        Optional<UserProfile> userProfile = userProfileService.getProfileByUserId(userId);
        return userProfile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Редактирование профиля
    @PutMapping("/{userId}")
    public ResponseEntity<UserProfile> updateProfile(@PathVariable UUID userId, @RequestBody UserProfile updatedProfile) {
        UserProfile updatedUserProfile = userProfileService.updateProfile(userId, updatedProfile);
        return ResponseEntity.ok(updatedUserProfile);
    }

    // Загрузка аватарки
    @PostMapping("/{userId}/avatar")
    public ResponseEntity<UserPhoto> uploadAvatar(@PathVariable UUID userId, @RequestParam String photoUrl) {
        UserPhoto userPhoto = userProfileService.uploadAvatar(userId, photoUrl);
        return ResponseEntity.ok(userPhoto);
    }

    // Добавление активности
    @PostMapping("/{userId}/activities")
    public ResponseEntity<UserActivity> addActivity(@PathVariable UUID userId, @RequestBody UserActivity userActivity) {
        UserActivity createdActivity = userProfileService.addActivity(userId, userActivity);
        return ResponseEntity.ok(createdActivity);
    }

    // Получение активностей
    @GetMapping("/{userId}/activities")
    public ResponseEntity<List<UserActivity>> getUserActivities(@PathVariable UUID userId) {
        List<UserActivity> activities = userProfileService.getUserActivities(userId);
        return ResponseEntity.ok(activities);
    }
}
