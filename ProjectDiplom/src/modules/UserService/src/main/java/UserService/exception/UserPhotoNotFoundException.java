// UserPhotoNotFoundException.java
package UserService.exception;

import java.util.UUID;

public class UserPhotoNotFoundException extends RuntimeException {
    public UserPhotoNotFoundException(UUID photoId) {
        super("Photo not found with ID: " + photoId);
    }

    public UserPhotoNotFoundException(String message) {
        super(message);
    }
}