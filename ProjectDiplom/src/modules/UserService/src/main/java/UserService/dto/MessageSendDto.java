package UserService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class MessageSendDto {
    @NotNull(message = "Chat ID is required")
    private UUID chatId;

    @NotNull(message = "Sender ID is required")
    private UUID senderId;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    private UUID replyTo;
}
