package UserService.service.impl;

import UserService.dto.*;
import UserService.exception.*;
import UserService.model.*;
import UserService.repository.*;
import UserService.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ChatParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ChatTypeRepository chatTypeRepository;


    @Override
    @Transactional
    public ChatDto createChat(ChatCreateDto createDto) throws UserNotFoundException {
        // Маппинг основных полей из DTO
        Chat chat = modelMapper.map(createDto, Chat.class);

        // Обработка типа чата через отдельную таблицу
        ChatTypeEntity chatType = chatTypeRepository.findByName(createDto.getType().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat type: " + createDto.getType()));

        // Установка дополнительных полей
        chat.setType(chatType);
        chat.setCreatedAt(LocalDateTime.now());

        // Сохранение чата
        Chat savedChat = chatRepository.save(chat);

        // Обработка участников
        for (UUID participantId : createDto.getParticipantIds()) {
            User user = userRepository.findById(participantId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + participantId));

            ChatParticipant participant = new ChatParticipant();
            participant.setChat(savedChat);
            participant.setUser(user);
            participant.setJoinedAt(LocalDateTime.now());
            participant.setLastReadAt(LocalDateTime.now());
            participantRepository.save(participant);
        }

        // Маппинг результата с дополнительными полями
        ChatDto resultDto = modelMapper.map(savedChat, ChatDto.class);
        resultDto.setType(ChatType.valueOf(chatType.getName()));

        return resultDto;
    }

    @Override
    public List<MessageDto> getChatMessages(UUID chatId, Pageable pageable) {
        return messageRepository.findByChatIdOrderBySentAtDesc(chatId, pageable)
                .stream()
                .map(msg -> modelMapper.map(msg, MessageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MessageDto sendMessage(UUID chatId, UUID senderId, MessageSendDto sendDto)
            throws ChatNotFoundException, UserNotFoundException, MessageNotFoundException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Chat not found with ID: " + chatId));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + senderId));

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(sendDto.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);

        if (sendDto.getReplyTo() != null) {
            Message replyTo = messageRepository.findById(sendDto.getReplyTo())
                    .orElseThrow(() -> new MessageNotFoundException("Message not found with ID: " + sendDto.getReplyTo()));
            message.setReplyTo(replyTo);
        }

        Message saved = messageRepository.save(message);
        return modelMapper.map(saved, MessageDto.class);
    }

    @Override
    public List<ChatDto> getUserChats(UUID userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        return participantRepository.findByUser_Id(userId)
                .stream()
                .map(participant -> {
                    Chat chat = participant.getChat();
                    ChatDto dto = modelMapper.map(chat, ChatDto.class);

                    Optional<Message> lastMessage = messageRepository.findTopByChatIdOrderBySentAtDesc(chat.getId());
                    lastMessage.ifPresent(message -> dto.setLastMessage(modelMapper.map(message, MessageDto.class)));

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChatDto addParticipant(UUID chatId, UUID userId)
            throws ChatNotFoundException, UserNotFoundException, ChatAccessDeniedException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException("Chat not found with ID: " + chatId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if (participantRepository.existsByChatIdAndUserId(chatId, userId)) {
            throw new ChatAccessDeniedException("User " + userId + " is already a participant in chat " + chatId);
        }

        ChatParticipant participant = new ChatParticipant();
        participant.setChat(chat);
        participant.setUser(user);
        participant.setJoinedAt(LocalDateTime.now());
        participantRepository.save(participant);

        return modelMapper.map(chat, ChatDto.class);
    }

    @Override
    @Transactional
    public void markMessagesAsRead(UUID chatId, UUID userId)
            throws ChatNotFoundException, UserNotFoundException {
        if (!chatRepository.existsById(chatId)) {
            throw new ChatNotFoundException("Chat not found with ID: " + chatId);
        }
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        messageRepository.markMessagesAsRead(chatId, userId, LocalDateTime.now());
    }
}