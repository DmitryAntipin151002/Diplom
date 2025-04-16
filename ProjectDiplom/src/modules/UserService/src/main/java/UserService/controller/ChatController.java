package UserService.controller;

import UserService.dto.*;
import UserService.exception.*;
import UserService.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    /**
     * Создание нового чата
     * @param createDto DTO с данными для создания чата
     * @return созданный чат или 404 если пользователь не найден
     */
    @PostMapping
    public ResponseEntity<ChatDto> createChat(@Valid @RequestBody ChatCreateDto createDto) {
        try {
            if (!isValidChatType(createDto.getType())) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChat(createDto));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private boolean isValidChatType(String type) {
        return Set.of("PRIVATE", "GROUP", "EVENT").contains(type.toUpperCase());
    }

    /**
     * Получение сообщений чата с пагинацией
     * @param chatId ID чата
     * @param pageable параметры пагинации
     * @return список сообщений
     */
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageDto>> getChatMessages(
            @PathVariable UUID chatId,
            @PageableDefault(size = 20) Pageable pageable) {
        List<MessageDto> messages = chatService.getChatMessages(chatId, pageable);
        return ResponseEntity.ok(messages);
    }

    /**
     * Отправка сообщения в чат
     * @param chatId ID чата
     * @param senderId ID отправителя (из заголовка)
     * @param sendDto DTO сообщения
     * @return отправленное сообщение или 404 если что-то не найдено
     */
    @PostMapping("/{chatId}/messages")
    public ResponseEntity<MessageDto> sendMessage(
            @PathVariable UUID chatId,
            @RequestHeader("X-User-Id") UUID senderId,
            @RequestBody MessageSendDto sendDto) {
        try {
            MessageDto messageDto = chatService.sendMessage(chatId, senderId, sendDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
        } catch (ChatNotFoundException | UserNotFoundException | MessageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Получение всех чатов пользователя
     * @param userId ID пользователя
     * @return список чатов или 404 если пользователь не найден
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatDto>> getUserChats(@PathVariable UUID userId) {
        try {
            List<ChatDto> chats = chatService.getUserChats(userId);
            return ResponseEntity.ok(chats);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Добавление участника в чат
     * @param chatId ID чата
     * @param userId ID пользователя (в теле запроса)
     * @return обновленный чат или код ошибки
     */
    @PostMapping("/{chatId}/participants")
    public ResponseEntity<ChatDto> addParticipant(
            @PathVariable UUID chatId,
            @RequestBody UUID userId) {
        try {
            ChatDto chatDto = chatService.addParticipant(chatId, userId);
            return ResponseEntity.ok(chatDto);
        } catch (ChatNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ChatAccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Пометка сообщений как прочитанных
     * @param chatId ID чата
     * @param userId ID пользователя (из заголовка)
     * @return 200 OK или 404 если что-то не найдено
     */
    @PostMapping("/{chatId}/mark-read")
    public ResponseEntity<Void> markMessagesAsRead(
            @PathVariable UUID chatId,
            @RequestHeader("X-User-Id") UUID userId) {
        try {
            chatService.markMessagesAsRead(chatId, userId);
            return ResponseEntity.ok().build();
        } catch (ChatNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}