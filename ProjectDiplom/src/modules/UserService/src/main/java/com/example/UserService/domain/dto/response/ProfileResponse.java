package dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileResponse {
    private String email;
    private String fullName;
    private String photoUrl;
    private String interests;
    private String preferredSports;
    private LocalDateTime createdAt;
}
