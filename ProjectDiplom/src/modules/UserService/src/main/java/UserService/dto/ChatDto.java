package UserService.dto;

import UserService.model.ChatType;
import UserService.model.ChatTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto{
    private UUID id;
    private String name;
    private ChatType type;
    private UUID eventId;
    private LocalDateTime createdAt;
    private List<UUID> participantIds;
    private MessageDto lastMessage;
    private int unreadCount;

}
