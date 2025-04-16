package UserService.dto;


import lombok.Data;
import java.util.UUID;

@Data
public class NotificationCreateDto {
    private UUID userId;
    private String title;
    private String message;
    private String type;
    private String relatedEntityType;
    private UUID relatedEntityId;
}