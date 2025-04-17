package UserService.service.impl;


import UserService.dto.AttachmentRequest;
import UserService.model.Message;
import UserService.model.MessageAttachment;
import UserService.repository.MessageAttachmentRepository;
import UserService.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttachmentServiceImpl implements AttachmentService {
    private final MessageAttachmentRepository messageAttachmentRepository;

    @Override
    public List<MessageAttachment> saveAttachments(Message message, List<AttachmentRequest> attachments) {
        return attachments.stream()
                .map(attachment -> {
                    MessageAttachment messageAttachment = new MessageAttachment();
                    messageAttachment.setMessage(message);
                    messageAttachment.setFileUrl(attachment.getFileUrl());
                    messageAttachment.setFileName(attachment.getFileName());
                    messageAttachment.setFileType(attachment.getFileType());
                    messageAttachment.setFileSize(attachment.getFileSize());
                    return messageAttachmentRepository.save(messageAttachment);
                })
                .collect(Collectors.toList());
    }
}