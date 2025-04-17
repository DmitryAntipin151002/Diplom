package UserService.dto;


import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class SendMessageRequest {
    private UUID chatId;
    private UUID senderId;
    private String content;
    private UUID replyToId;
    private List<AttachmentRequest> attachments;
}
