package UserService.dto;



import lombok.Data;

import java.util.List;
import java.util.UUID;


/**
 * DTO для создания чата
 */
@Data
public class ChatCreateDto {
    private String name;
    private String type;
    private UUID eventId;
    private List<UUID> participantIds;
}
