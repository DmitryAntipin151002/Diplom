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
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users/{userId}/photos")
@RequiredArgsConstructor
public class UserPhotoController {
    private final UserPhotoService userPhotoService;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<UserPhoto> uploadPhotoByUrl(
            @PathVariable UUID userId,
            @RequestParam String photoUrl,
            @RequestParam(required = false) String description) {
        UserPhoto photo = userPhotoService.uploadPhotoByUrl(userId, photoUrl, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(photo);
    }

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

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(
            @PathVariable UUID userId,
            @PathVariable UUID photoId) {
        // Проверяем, не используется ли фото как аватар
        UserPhoto photo = userPhotoService.getPhoto(userId, photoId);
        if (photo.isProfilePhoto()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot delete photo used as profile avatar"
            );
        }

        // Удаляем файл если он локальный
        if (photo.getPhotoUrl().startsWith("/api/files/")) {
            String filePath = photo.getPhotoUrl().replace("/api/files/", "");
            fileStorageService.delete(filePath);
        }

        // Удаляем запись из БД
        userPhotoService.deletePhoto(userId, photoId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{photoId}/profile")
    public ResponseEntity<PhotoResponseDto> setAsProfilePhoto(
            @PathVariable UUID userId,
            @PathVariable UUID photoId) {
        UserPhoto photo = userPhotoService.setAsProfilePhoto(userId, photoId);

        PhotoResponseDto response = PhotoResponseDto.builder()
                .id(photo.getId())
                .photoUrl(photo.getPhotoUrl())
                .description(photo.getDescription())
                .isProfilePhoto(photo.isProfilePhoto())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PhotoResponseDto>> getUserPhotos(@PathVariable UUID userId) {
        List<UserPhoto> photos = userPhotoService.getUserPhotos(userId);

        List<PhotoResponseDto> response = photos.stream()
                .map(photo -> PhotoResponseDto.builder()
                        .id(photo.getId())
                        .photoUrl(photo.getPhotoUrl())
                        .description(photo.getDescription())
                        .isProfilePhoto(photo.isProfilePhoto())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<PhotoResponseDto> getProfilePhoto(@PathVariable UUID userId) {
        UserPhoto photo = userPhotoService.getProfilePhoto(userId);

        PhotoResponseDto response = PhotoResponseDto.builder()
                .id(photo.getId())
                .photoUrl(photo.getPhotoUrl())
                .description(photo.getDescription())
                .isProfilePhoto(photo.isProfilePhoto())
                .build();

        return ResponseEntity.ok(response);
    }
}