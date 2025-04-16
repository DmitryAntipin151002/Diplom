package UserService.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class MessageSendDto {
    private String content;
    private UUID replyTo;
}
