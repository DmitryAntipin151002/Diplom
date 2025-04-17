package UserService.dto;

import UserService.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private UUID id;
    private UUID chatId;
    private UUID senderId;
    private String senderName;
    private String content;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private UUID replyToId;
    private Message.MessageStatus status;
    private List<AttachmentDTO> attachments;
}
