package UserService.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ChatParticipantsUpdateDTO {
    private UUID chatId;
    private List<UUID> participantIds;
    private boolean added; // true - добавлены, false - удалены

    public ChatParticipantsUpdateDTO(UUID chatId, List<UUID> participantIds, boolean added) {
        this.chatId = chatId;
        this.participantIds = participantIds;
        this.added = added;
    }
}