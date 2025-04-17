package UserService.service;


import UserService.dto.AttachmentRequest;
import UserService.model.Message;
import UserService.model.MessageAttachment;
import java.util.List;

public interface AttachmentService {
    List<MessageAttachment> saveAttachments(Message message, List<AttachmentRequest> attachments);
}