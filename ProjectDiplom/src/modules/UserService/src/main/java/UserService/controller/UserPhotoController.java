package UserService.controller;

import UserService.model.UserPhoto;
import UserService.service.UserPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userId}/photos")
@RequiredArgsConstructor
public class UserPhotoController {

    private final UserPhotoService userPhotoService;

    /**
     * Загрузить новую фотографию
     * @param userId ID пользователя
     * @param photoUrl URL фотографии
     * @param description Описание фотографии
     * @return Загруженная фотография
     */
    @PostMapping
    public ResponseEntity<UserPhoto> uploadPhoto(
            @PathVariable UUID userId,
            @RequestParam String photoUrl,
            @RequestParam(required = false) String description) {
        UserPhoto photo = userPhotoService.uploadPhoto(userId, photoUrl, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(photo);
    }

    /**
     * Удалить фотографию
     * @param userId ID пользователя
     * @param photoId ID фотографии
     * @return Пустой ответ со статусом 204
     */
    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable UUID userId,
            @PathVariable UUID photoId) {
        userPhotoService.deletePhoto(userId, photoId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Установить фотографию как профильную
     * @param userId ID пользователя
     * @param photoId ID фотографии
     * @return Обновленная фотография профиля
     */
    @PatchMapping("/{photoId}/profile")
    public ResponseEntity<UserPhoto> setAsProfilePhoto(
            @PathVariable UUID userId,
            @PathVariable UUID photoId) {
        UserPhoto photo = userPhotoService.setAsProfilePhoto(userId, photoId);
        return ResponseEntity.ok(photo);
    }

    /**
     * Получить все фотографии пользователя
     * @param userId ID пользователя
     * @return Список фотографий пользователя
     */
    @GetMapping
    public ResponseEntity<List<UserPhoto>> getUserPhotos(@PathVariable UUID userId) {
        List<UserPhoto> photos = userPhotoService.getUserPhotos(userId);
        return ResponseEntity.ok(photos);
    }

    /**
     * Получить профильную фотографию пользователя
     * @param userId ID пользователя
     * @return Профильная фотография
     */
    @GetMapping("/profile")
    public ResponseEntity<UserPhoto> getProfilePhoto(@PathVariable UUID userId) {
        UserPhoto photo = userPhotoService.getProfilePhoto(userId);
        return ResponseEntity.ok(photo);
    }

    /**
     * Загрузить фотографию с файлом (альтернативный вариант)
     * @param userId ID пользователя
     * @param file Файл фотографии
     * @param description Описание фотографии
     * @return Загруженная фотография
     */
    @PostMapping("/upload")
    public ResponseEntity<UserPhoto> uploadPhotoFile(
            @PathVariable UUID userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description) {
        // Здесь должна быть логика сохранения файла и получения URL
        // Например:
        // String photoUrl = fileStorageService.store(file);
        // UserPhoto photo = userPhotoService.uploadPhoto(userId, photoUrl, description);
        // return ResponseEntity.status(HttpStatus.CREATED).body(photo);
        throw new UnsupportedOperationException("File upload not implemented yet");
    }
}