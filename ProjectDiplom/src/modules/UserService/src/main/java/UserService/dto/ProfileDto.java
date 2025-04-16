package UserService.dto;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProfileDto {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String bio;
    private String sportType;
    private String location;
    private String website;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}