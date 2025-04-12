package EventService.model;

import lombok.Data;

import java.util.UUID;

@Data
public class UserProfile {
    private UUID userId;
    private String avatarUrl;
    private String bio;
}
