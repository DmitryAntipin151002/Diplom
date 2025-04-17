package UserService.service;

import UserService.dto.*;
import UserService.exception.*;
import UserService.model.Message;
import UserService.model.MessageAttachment;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto sendMessage(SendMessageRequest request)
            throws ChatNotFoundException, UserNotFoundException;

    List<MessageDto> getChatMessages(UUID chatId, UUID userId, int page, int size)
            throws ChatNotFoundException;

    void markMessagesAsRead(UUID chatId, UUID userId)
            throws ChatNotFoundException;

    MessageDto editMessage(UUID messageId, String newContent, UUID editorId)
            throws MessageNotFoundException;

    void deleteMessage(UUID messageId, UUID deleterId)
            throws MessageNotFoundException;

    MessageDto convertToDto(Message message);

    AttachmentDTO convertAttachmentToDto(MessageAttachment attachment);
}