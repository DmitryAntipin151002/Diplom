package UserService.service.impl;

import UserService.config.ModelMapperConfig;
import UserService.dto.*;
import UserService.exception.*;
import UserService.model.*;
import UserService.repository.*;
import UserService.service.ChatService;
import UserService.service.EventService;
import UserService.service.MessageService;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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
    private final ChatParticipantRepository chatParticipantRepository;
    private final ModelMapperConfig modelMapper;

    @Override
    @Transactional
    public ChatDto createEventChat(UUID eventId, String chatName, UUID creatorId) {
        // Создание чата
        Chat chat = new Chat();
        chat.setName(chatName);
        chat.setEvent(eventRepository.findById(eventId).orElseThrow());
        chat.setType(chatTypeRepository.findByCode("EVENT").orElseThrow());
        Chat savedChat = chatRepository.save(chat); // Сохраняем чат сначала

        // Добавление создателя в участники
        User creator = userRepository.findById(creatorId).orElseThrow();

        ChatParticipant participant = new ChatParticipant();
        participant.setChat(savedChat); // Используем сохраненный чат с ID
        participant.setUser(creator);
        participant.setJoinedAt(LocalDateTime.now());

        chatParticipantRepository.save(participant); // Сохраняем участника

        return convertToDto(savedChat);
    }

    @Override
    public ChatDto createChat(CreateChatRequest request) {
        // Получаем тип чата
        ChatTypeEntity chatType = chatTypeRepository.findByName(request.getType().name())
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat type: " + request.getType()));

        // Создаем чат
        Chat chat = new Chat();
        chat.setName(request.getName());
        chat.setType(chatType);
        chat.setCreatedAt(LocalDateTime.now());

        // Сохраняем чат
        Chat savedChat = chatRepository.save(chat);

        // Добавляем создателя
        User creator = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("Creator not found: " + request.getCreatorId()));

        addParticipant(savedChat, creator, true);

        // Добавляем участников
        request.getParticipantIds().forEach(userId -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
            addParticipant(savedChat, user, false);
        });

        return convertToDto(savedChat);
    }

    private void addParticipant(Chat chat, User user, boolean isAdmin) {
        ChatParticipant participant = new ChatParticipant();
        participant.setChat(chat);
        participant.setUser(user);
        participant.setJoinedAt(LocalDateTime.now());
        participant.setAdmin(isAdmin);

        // Явное сохранение
        chatParticipantRepository.save(participant);

        // Для двусторонней связи
        chat.getParticipants().add(participant);
        user.getChatParticipants().add(participant);
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
        log.info("Checking access for chat: {} and user: {}", chatId, userId);
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));

        if (!chatRepository.existsByIdAndParticipants_User_Id(chatId, userId)) {
            throw new ChatNotFoundException("User is not a participant of this chat");
        }
        boolean exists = chatRepository.existsByIdAndParticipants_User_Id(chatId, userId);
        log.debug("Participant exists: {}", exists);

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
        dto.setType(ChatType.valueOf(chat.getType().getCode()));
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


    @PostConstruct
    public void initChatTypes() {
        createChatTypeIfNotExists("EVENT", "Чат мероприятия");
        createChatTypeIfNotExists("GROUP", "Групповой чат");
        createChatTypeIfNotExists("PRIVATE", "Приватный чат");
    }

    private void createChatTypeIfNotExists(String code, String name) {
        if (!chatTypeRepository.existsByCode(code)) {
            ChatTypeEntity type = new ChatTypeEntity();
            type.setCode(code);
            type.setName(name);
            chatTypeRepository.save(type);
        }
    }
}