package UserService.service;

import UserService.model.UserPhoto;
import java.util.List;
import java.util.UUID;

public interface UserPhotoService {
    UserPhoto uploadPhoto(UUID userId, String photoUrl, String description);
    void deletePhoto(UUID userId, UUID photoId);
    UserPhoto setAsProfilePhoto(UUID userId, UUID photoId);
    List<UserPhoto> getUserPhotos(UUID userId);
    UserPhoto getProfilePhoto(UUID userId);
}