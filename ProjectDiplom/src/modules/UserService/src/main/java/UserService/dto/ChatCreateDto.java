package UserService.dto;



import UserService.model.Chat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * DTO для создания чата
 */
@Data
public class ChatCreateDto {
    private String name;
    private Chat.ChatType type;
    private UUID eventId;
    private List<UUID> participantIds;
}
