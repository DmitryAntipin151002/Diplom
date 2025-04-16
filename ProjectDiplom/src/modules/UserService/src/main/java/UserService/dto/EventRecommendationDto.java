package UserService.dto;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EventRecommendationDto {
    private UUID id;
    private String title;
    private String sportType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private UUID organizerId;
    private String organizerName;
    private int participantsCount;
    private double matchScore; // Оценка соответствия (0-1)
}