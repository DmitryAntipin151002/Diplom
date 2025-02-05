package UserService.domain.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionDTO {
    private Long id;
    private Integer userId;
    private String sportType;
    private LocalDateTime subscribedAt;
}

