package dto;
import dto.ProfileDTO;
import dto.SubscriptionDTO;
import dto.UserActivityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private Integer userId;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;
    private List<ProfileDTO> profiles;
    private List<FriendDTO> friends;
    private List<SubscriptionDTO> subscriptions;
    private List<UserActivityDTO> userActivities;
}
