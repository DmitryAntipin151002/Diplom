package UserService.service.impl;

import UserService.exception.UserNotFoundException;
import UserService.exception.UserPhotoNotFoundException;
import UserService.model.User;
import UserService.model.UserPhoto;
import UserService.repository.UserPhotoRepository;
import UserService.repository.UserRepository;
import UserService.service.UserPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPhotoServiceImpl implements UserPhotoService {
    private final UserPhotoRepository photoRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserPhoto uploadPhoto(UUID userId, String photoUrl, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        UserPhoto photo = new UserPhoto();
        photo.setUser(user);
        photo.setPhotoUrl(photoUrl);
        photo.setDescription(description);

        return photoRepository.save(photo);
    }

    @Override
    @Transactional
    public void deletePhoto(UUID userId, UUID photoId) {
        if (!photoRepository.existsByUserIdAndId(userId, photoId)) {
            throw new UserPhotoNotFoundException(photoId);
        }
        photoRepository.deleteByUserIdAndId(userId, photoId);
    }

    @Override
    @Transactional
    public UserPhoto setAsProfilePhoto(UUID userId, UUID photoId) {
        // Сначала сбросим текущую фотографию профиля
        photoRepository.findByUserIdAndIsProfilePhotoTrue(userId)
                .ifPresent(photo -> {
                    photo.setProfilePhoto(false);
                    photoRepository.save(photo);
                });

        // Установим новую фотографию профиля
        UserPhoto photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new UserPhotoNotFoundException(photoId));

        if (!photo.getUser().getId().equals(userId)) {
            throw new UserPhotoNotFoundException(photoId);
        }

        photo.setProfilePhoto(true);
        return photoRepository.save(photo);
    }

    @Override
    public List<UserPhoto> getUserPhotos(UUID userId) {
        return photoRepository.findByUserId(userId);
    }

    @Override
    public UserPhoto getProfilePhoto(UUID userId) {
        return photoRepository.findByUserIdAndIsProfilePhotoTrue(userId)
                .orElseThrow(() -> new UserPhotoNotFoundException("Profile photo not found"));
    }
}