package UserService.dto;

import UserService.model.Message;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MessageStatusUpdateDTO {
    private List<UUID> messageIds;
    private Message.MessageStatus status;

    public MessageStatusUpdateDTO(List<UUID> messageIds, Message.MessageStatus status) {
        this.messageIds = messageIds;
        this.status = status;
    }
}