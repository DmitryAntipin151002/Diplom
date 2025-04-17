package UserService.service.impl;

import UserService.dto.*;
import UserService.exception.*;
import UserService.model.*;
import UserService.repository.*;
import UserService.service.AttachmentService;
import UserService.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final AttachmentService attachmentService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public MessageDto sendMessage(SendMessageRequest request)
            throws ChatNotFoundException, UserNotFoundException {
        Chat chat = chatRepository.findById(request.getChatId())
                .orElseThrow(() -> new ChatNotFoundException(request.getChatId()));
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new UserNotFoundException(request.getSenderId()));

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(request.getContent());

        if (request.getReplyToId() != null) {
            Message replyTo = messageRepository.findById(request.getReplyToId())
                    .orElseThrow(() -> new MessageNotFoundException(request.getReplyToId()));
            message.setReplyTo(replyTo);
        }

        log.info("Saving message: {}", message);
        message = messageRepository.save(message);
        log.info("Message saved with ID: {}", message.getId());

        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            List<MessageAttachment> attachments = attachmentService.saveAttachments(message, request.getAttachments());
            message.setAttachments(attachments);
        }

        MessageDto messageDTO = convertToDto(message);

        messagingTemplate.convertAndSend("/topic/chat/" + chat.getId(), messageDTO);

        chat.getParticipants().forEach(participant -> {
            if (!participant.getUser().getId().equals(sender.getId())) {
                messagingTemplate.convertAndSendToUser(
                        participant.getUser().getId().toString(),
                        "/queue/chat-updates",
                        new ChatUpdateDTO(chat.getId(), messageDTO)
                );
            }
        });

        return messageDTO;
    }

    @Override
    public List<MessageDto> getChatMessages(UUID chatId, UUID userId, int page, int size)
            throws ChatNotFoundException{
        if (!chatRepository.existsById(chatId)) {
            throw new ChatNotFoundException(chatId);
        }

        if (!chatRepository.existsByIdAndParticipants_User_Id(chatId, userId)) {
            throw new ChatNotFoundException ("User is not a participant of this chat");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages = messageRepository.findByChatIdOrderBySentAtDesc(chatId, pageable);

        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void markMessagesAsRead(UUID chatId, UUID userId)
            throws ChatNotFoundException {
        if (!chatRepository.existsById(chatId)) {
            throw new ChatNotFoundException(chatId);
        }

        List<Message> unreadMessages = messageRepository.findUnreadMessages(chatId, userId);
        if (!unreadMessages.isEmpty()) {
            messageRepository.markMessagesAsRead(
                    unreadMessages.stream().map(Message::getId).collect(Collectors.toList()),
                    LocalDateTime.now()
            );

            unreadMessages.stream()
                    .collect(Collectors.groupingBy(Message::getSender))
                    .forEach((sender, messages) -> {
                        if (!sender.getId().equals(userId)) {
                            messagingTemplate.convertAndSendToUser(
                                    sender.getId().toString(),
                                    "/queue/message-updates",
                                    new MessageStatusUpdateDTO(
                                            messages.stream().map(Message::getId).collect(Collectors.toList()),
                                            Message.MessageStatus.READ
                                    )
                            );
                        }
                    });
        }
    }

    @Override
    public MessageDto editMessage(UUID messageId, String newContent, UUID editorId)
            throws MessageNotFoundException {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        if (!message.getSender().getId().equals(editorId)) {
            throw new MessageNotFoundException("Only message author can edit the message");
        }

        message.setContent(newContent);
        message.setStatus(Message.MessageStatus.EDITED);
        message = messageRepository.save(message);

        MessageDto messageDTO = convertToDto(message);
        messagingTemplate.convertAndSend("/topic/chat/" + message.getChat().getId(), messageDTO);

        return messageDTO;
    }

    @Override
    public void deleteMessage(UUID messageId, UUID deleterId)
            throws MessageNotFoundException{
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));

        // Проверяем, что удаляющий - либо автор, либо администратор чата
        boolean isAuthor = message.getSender().getId().equals(deleterId);
        boolean isAdmin = chatRepository.isUserAdmin(message.getChat().getId(), deleterId);

        if (!isAuthor && !isAdmin) {
            throw new MessageNotFoundException("No permission to delete this message");
        }

        message.setStatus(Message.MessageStatus.DELETED);
        messageRepository.save(message);

        MessageDto messageDTO = convertToDto(message);
        messagingTemplate.convertAndSend("/topic/chat/" + message.getChat().getId(), messageDTO);
    }

    @Override
    public MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setChatId(message.getChat().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());
        dto.setReplyToId(message.getReplyTo() != null ? message.getReplyTo().getId() : null);
        dto.setStatus(message.getStatus());

        if (message.getAttachments() != null) {
            dto.setAttachments(message.getAttachments().stream()
                    .map(this::convertAttachmentToDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    @Override
    public AttachmentDTO convertAttachmentToDto(MessageAttachment attachment) {
        return new AttachmentDTO(
                attachment.getFileUrl(),
                attachment.getFileName(),
                attachment.getFileType()
        );
    }
}