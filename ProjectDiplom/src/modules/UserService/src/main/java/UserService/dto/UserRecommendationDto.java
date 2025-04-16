package UserService.dto;



import lombok.Data;
import java.util.UUID;

@Data
public class UserRecommendationDto {
    private UUID id;
    private String username;
    private String avatarUrl;
    private String sportType;
    private String location;
    private int mutualFriendsCount;
    private double matchScore; // Оценка соответствия (0-1)
}
