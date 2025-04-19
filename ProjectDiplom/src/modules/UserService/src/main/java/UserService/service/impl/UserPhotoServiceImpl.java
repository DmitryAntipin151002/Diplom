package UserService.service.impl;

import UserService.exception.UserNotFoundException;
import UserService.exception.UserPhotoNotFoundException;
import UserService.model.User;
import UserService.model.UserPhoto;
import UserService.repository.UserPhotoRepository;
import UserService.repository.UserRepository;
import UserService.service.FileStorageService;
import UserService.service.UserPhotoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UserPhotoServiceImpl implements UserPhotoService {
    private final UserPhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public UserPhoto uploadPhotoByUrl(UUID userId, String photoUrl, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        UserPhoto photo = UserPhoto.builder()
                .user(user)
                .photoUrl(photoUrl)
                .description(description)
                .isProfilePhoto(false)
                .build();

        return photoRepository.save(photo);
    }

    @Override
    @Transactional
    public UserPhoto uploadPhotoFile(UUID userId, MultipartFile file, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String filePath = fileStorageService.store(file, userId);
        String photoUrl = "/api/files/" + filePath;

        UserPhoto photo = UserPhoto.builder()
                .user(user)
                .photoUrl(photoUrl)
                .description(description)
                .isProfilePhoto(false)
                .build();

        return photoRepository.save(photo);
    }

    @Override
    @Transactional
    public void deletePhoto(UUID userId, UUID photoId) {
        if (!isPhotoBelongsToUser(userId, photoId)) {
            throw new UserPhotoNotFoundException(photoId);
        }
        photoRepository.deleteById(photoId);
    }

    @Override
    @Transactional
    public UserPhoto setAsProfilePhoto(UUID userId, UUID photoId) {
        if (!isPhotoBelongsToUser(userId, photoId)) {
            throw new UserPhotoNotFoundException(photoId);
        }

        // Сброс предыдущей профильной фото
        photoRepository.findByUserIdAndIsProfilePhotoTrue(userId)
                .ifPresent(photo -> {
                    photo.setProfilePhoto(false);
                    photoRepository.save(photo);
                });

        // Установка новой профильной фото
        UserPhoto newProfilePhoto = photoRepository.findById(photoId)
                .orElseThrow(() -> new UserPhotoNotFoundException(photoId));
        newProfilePhoto.setProfilePhoto(true);

        return photoRepository.save(newProfilePhoto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPhoto> getUserPhotos(UUID userId) {
        return photoRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserPhoto getProfilePhoto(UUID userId) {
        return photoRepository.findByUserIdAndIsProfilePhotoTrue(userId)
                .orElseThrow(() -> new UserPhotoNotFoundException("Profile photo not found for user: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public UserPhoto getPhoto(UUID userId, UUID photoId) {
        return photoRepository.findByUserIdAndId(userId, photoId)
                .orElseThrow(() -> new UserPhotoNotFoundException(photoId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPhotoBelongsToUser(UUID userId, UUID photoId) {
        return photoRepository.existsByUserIdAndId(userId, photoId);
    }
}