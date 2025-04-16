package UserService.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventCreateDto {
    private String title;
    private String description;
    private String statusCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;


}
