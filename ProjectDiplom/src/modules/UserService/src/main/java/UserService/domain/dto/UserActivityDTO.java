package UserService.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserActivityDTO {
    private Integer id;
    private Integer userId;
    private Integer eventsCreated;
    private Integer eventsParticipated;
    private LocalDateTime lastActive;
}
