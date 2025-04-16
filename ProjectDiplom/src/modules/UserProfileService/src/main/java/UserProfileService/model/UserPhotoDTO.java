package UserProfileService.model;

import java.time.LocalDateTime;
import java.util.UUID;

// UserPhotoDTO.java
public record UserPhotoDTO(UUID id, String photoUrl, boolean isMain, LocalDateTime uploadedAt) {}