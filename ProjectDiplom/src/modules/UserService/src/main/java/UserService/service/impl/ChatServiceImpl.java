package UserService.service.impl;

import UserService.dto.*;
import UserService.exception.*;
import UserService.model.*;
import UserService.repository.*;
import UserService.service.ChatService;
import UserService.service.MessageService;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatTypeRepository chatTypeRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final EventRepository eventRepository;
    private final MessageService messageService;

    @Override
    public ChatDto createChat(CreateChatRequest request) {
        // Валидация
        if (request.getParticipantIds().isEmpty()) {
            throw new BadRequestException("Chat must have participants");
        }

        Chat chat = new Chat();
        chat.setName(request.getName());
        chat.setType(chatTypeRepository.findByName(request.getType().name())
                .orElseThrow(() -> new RuntimeException("Invalid chat type")));

        // Сохраняем чат перед добавлением участников
        Chat savedChat = chatRepository.save(chat);

        // Создаем участников
        List<ChatParticipant> participants = request.getParticipantIds().stream()
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found: " + userId));
                    return createParticipant(savedChat, user);
                })
                .collect(Collectors.toList());

        savedChat.setParticipants(participants);
        return convertToDto(chatRepository.save(savedChat));
    }

    private ChatParticipant createParticipant(Chat chat, User user) {
        ChatParticipant participant = new ChatParticipant();
        participant.setChat(chat);
        participant.setUser(user);
        participant.setJoinedAt(LocalDateTime.now());
        participant.setAdmin(false);
        return participant;
    }

    @Override
    public List<ChatDto> getUserChats(UUID userId) {
        return chatRepository.findByParticipants_User_Id(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChatDto getChatInfo(UUID chatId, UUID userId) throws ChatNotFoundException{
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));

        if (!chatRepository.existsByIdAndParticipants_User_Id(chatId, userId)) {
            throw new ChatNotFoundException("User is not a participant of this chat");
        }

        return convertToDto(chat);
    }

    @Override
    public void addParticipants(UUID chatId, List<UUID> participantIds, UUID requesterId)
            throws ChatNotFoundException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));

        if (!chatRepository.existsByIdAndParticipants_User_Id(chatId, requesterId)) {
            throw new ChatNotFoundException("Requester is not a participant of this chat");
        }

        participantIds.forEach(userId -> {
            if (!chatRepository.existsByIdAndParticipants_User_Id(chatId, userId)) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                addParticipant(chat, user);
            }
        });

        // Отправка уведомления о новых участниках
        messagingTemplate.convertAndSend("/topic/chat/" + chatId + "/participants",
                new ChatParticipantsUpdateDTO(chatId, participantIds, true));
    }

    @Override
    public void removeParticipant(UUID chatId, UUID participantId, UUID requesterId)
            throws ChatNotFoundException  {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));

        // Проверяем права: либо удаляет себя, либо админ
        boolean isSelfRemoval = requesterId.equals(participantId);
        boolean isAdmin = chatRepository.isUserAdmin(chatId, requesterId);

        if (!isSelfRemoval && !isAdmin) {
            throw new ChatNotFoundException("No permission to remove participant");
        }

        boolean removed = chat.getParticipants().removeIf(p ->
                p.getUser().getId().equals(participantId)
        );

        if (!removed) {
            throw new ChatNotFoundException("Participant not found in chat");
        }

        // Отправка уведомления об удалении участника
        messagingTemplate.convertAndSend("/topic/chat/" + chatId + "/participants",
                new ChatParticipantsUpdateDTO(chatId, List.of(participantId), false));
    }

    private void addParticipant(Chat chat, User user) {
        ChatParticipant participant = new ChatParticipant();
        participant.setChat(chat);
        participant.setUser(user);
        participant.setJoinedAt(LocalDateTime.now());
        participant.setAdmin(false); // По умолчанию не админ
        chat.getParticipants().add(participant);
    }

    private ChatDto convertToDto(Chat chat) {
        ChatDto dto = new ChatDto();
        dto.setId(chat.getId());
        dto.setName(chat.getName());
        dto.setType(ChatType.valueOf(chat.getType().getName()));
        dto.setCreatedAt(chat.getCreatedAt());
        dto.setEventId(chat.getEvent() != null ? chat.getEvent().getId() : null);
        dto.setParticipantIds(chat.getParticipants().stream()
                .map(p -> p.getUser().getId())
                .collect(Collectors.toList()));

        Message lastMessage = null;
        if (!chat.getMessages().isEmpty()) {
            lastMessage = chat.getMessages().get(chat.getMessages().size() - 1);
        }

        return dto;
    }
}