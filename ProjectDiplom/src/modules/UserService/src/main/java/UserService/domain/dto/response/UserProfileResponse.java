package UserService.domain.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserProfileResponse {
    private String email;
    private String fullName;
    private String photoUrl;
    private String interests;
    private String preferredSports;
    private LocalDateTime createdAt;
}
