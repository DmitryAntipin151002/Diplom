package UserService.dto;

import UserService.model.ChatType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateChatRequest {
    private String name;
    private ChatType type;  // PRIVATE, GROUP, EVENT
    private UUID eventId;   // Optional, only for EVENT type
    private List<UUID> participantIds;
}