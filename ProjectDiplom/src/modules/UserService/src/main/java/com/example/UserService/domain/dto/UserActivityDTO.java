package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityDTO {
    private Integer id;
    private Integer userId;
    private Integer eventsCreated;
    private Integer eventsParticipated;
    private LocalDateTime lastActive;


}

