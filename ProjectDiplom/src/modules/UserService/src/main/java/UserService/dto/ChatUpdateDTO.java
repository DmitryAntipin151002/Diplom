package UserService.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatUpdateDTO {
    private UUID chatId;
    private MessageDto message;

    public ChatUpdateDTO(UUID chatId, MessageDto message) {
        this.chatId = chatId;
        this.message = message;
    }
}
