package UserProfileService.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserActivityDTO {
    private String activityType;
    private BigDecimal distance;
    private String duration;
    private Integer caloriesBurned;
    private LocalDateTime activityDate;
}
