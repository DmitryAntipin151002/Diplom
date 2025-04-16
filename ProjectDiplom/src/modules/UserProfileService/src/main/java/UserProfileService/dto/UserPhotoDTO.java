package UserProfileService.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserPhotoDTO {
    private String photoUrl;
    private Boolean isMain;
    private String description;
}
