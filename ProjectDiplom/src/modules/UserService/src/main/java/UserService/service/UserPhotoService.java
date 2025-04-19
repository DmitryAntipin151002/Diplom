package UserService.service;

import UserService.model.UserPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserPhotoService {
    UserPhoto uploadPhotoByUrl(UUID userId, String photoUrl, String description);
    UserPhoto uploadPhotoFile(UUID userId, MultipartFile file, String description);
    void deletePhoto(UUID userId, UUID photoId);
    UserPhoto setAsProfilePhoto(UUID userId, UUID photoId);
    List<UserPhoto> getUserPhotos(UUID userId);
    UserPhoto getProfilePhoto(UUID userId);
    UserPhoto getPhoto(UUID userId, UUID photoId);
    boolean isPhotoBelongsToUser(UUID userId, UUID photoId);
}