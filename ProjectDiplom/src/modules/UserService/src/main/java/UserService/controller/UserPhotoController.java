package UserService.controller;

import UserService.dto.PhotoResponseDto;
import UserService.model.UserPhoto;
import UserService.service.UserPhotoService;
import UserService.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users/{userId}/photos")
@RequiredArgsConstructor
public class UserPhotoController {

    private final UserPhotoService userPhotoService;
    private final FileStorageService fileStorageService;

    /**
     * Загрузить фотографию по URL (если нужно сохранить эту функциональность)
     */
    @PostMapping
    public ResponseEntity<UserPhoto> uploadPhotoByUrl(
            @PathVariable UUID userId,
            @RequestParam String photoUrl,
            @RequestParam(required = false) String description) {
        UserPhoto photo = userPhotoService.uploadPhotoByUrl(userId, photoUrl, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(photo);
    }

    /**
     * Загрузить фотографию с файлом
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoResponseDto> uploadPhotoFile(
            @PathVariable UUID userId,
            @RequestPart("file") MultipartFile file,
            @RequestParam(required = false) String description) {

        try {
            UserPhoto photo = userPhotoService.uploadPhotoFile(userId, file, description);

            PhotoResponseDto response = PhotoResponseDto.builder()
                    .id(photo.getId())
                    .photoUrl(photo.getPhotoUrl())
                    .description(photo.getDescription())
                    .isProfilePhoto(photo.isProfilePhoto())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Удалить фотографию
     */
    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable UUID userId,
            @PathVariable UUID photoId) {

        // 1. Получаем фото для проверки прав доступа
        UserPhoto photo = userPhotoService.getPhoto(userId, photoId);

        // 2. Удаляем файл если он локальный
        if (photo.getPhotoUrl().startsWith("/api/files/")) {
            String filePath = photo.getPhotoUrl().replace("/api/files/" + userId + "/", "");
            fileStorageService.delete(userId + "/" + filePath);
        }

        // 3. Удаляем запись из БД
        userPhotoService.deletePhoto(userId, photoId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Установить как профильную
     */
    @PatchMapping("/{photoId}/profile")
    public ResponseEntity<UserPhoto> setAsProfilePhoto(
            @PathVariable UUID userId,
            @PathVariable UUID photoId) {
        UserPhoto photo = userPhotoService.setAsProfilePhoto(userId, photoId);
        return ResponseEntity.ok(photo);
    }

    /**
     * Получить все фотографии
     */
    @GetMapping
    public ResponseEntity<List<UserPhoto>> getUserPhotos(@PathVariable UUID userId) {
        List<UserPhoto> photos = userPhotoService.getUserPhotos(userId);
        return ResponseEntity.ok(photos);
    }

    /**
     * Получить профильную фотографию
     */
    @GetMapping("/profile")
    public ResponseEntity<UserPhoto> getProfilePhoto(@PathVariable UUID userId) {
        UserPhoto photo = userPhotoService.getProfilePhoto(userId);
        return ResponseEntity.ok(photo);
    }
}