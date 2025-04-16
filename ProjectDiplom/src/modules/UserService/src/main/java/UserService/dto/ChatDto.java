package UserService.dto;

import UserService.model.Chat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ChatDto {
    private UUID id;
    private String name;
    private Chat.ChatType type;
    private UUID eventId;
    private LocalDateTime createdAt;
    private List<ParticipantDto> participants;
    private MessageDto lastMessage;
}
