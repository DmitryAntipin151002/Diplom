package UserProfileService.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class UserProfileDTO {
    private UUID userId;
    private String avatarUrl;
    private String bio;
    private LocalDate dateOfBirth;
    private String gender;
    private String location;
    private String sportType;
    private String fitnessLevel;
    private String goals;
    private String achievements;
    private String personalRecords;
    private Boolean isVerified;
    private List<UserPhotoDTO> photos;
}
