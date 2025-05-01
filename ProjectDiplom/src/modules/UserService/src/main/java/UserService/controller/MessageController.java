package UserService.controller;

import UserService.dto.*;
import UserService.exception.*;
import UserService.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    // REST API endpoints
    @GetMapping("/api/chats/{chatId}/messages")
    public ResponseEntity<List<MessageDto>> getChatMessages(
            @PathVariable UUID chatId,
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) throws ChatNotFoundException, AccessDeniedException {
        return ResponseEntity.ok(messageService.getChatMessages(chatId, userId, page, size));
    }

    @PostMapping("/api/chats/{chatId}/messages/read")
    public ResponseEntity<Void> markMessagesAsRead(
            @PathVariable UUID chatId,
            @RequestParam UUID userId) throws ChatNotFoundException {
        messageService.markMessagesAsRead(chatId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/messages/{messageId}")
    public ResponseEntity<MessageDto> editMessage(
            @PathVariable UUID messageId,
            @RequestBody EditMessageRequest request) { // Используем DTO для запроса
        return ResponseEntity.ok(
                messageService.editMessage(
                        messageId,
                        request.getNewContent(),
                        request.getEditorId()
                )
        );
    }

    @DeleteMapping("/api/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable UUID messageId,
            @RequestBody DeleteMessageRequest request) { // Используем DTO для запроса
        messageService.deleteMessage(messageId, request.getDeleterId());
        return ResponseEntity.ok().build();
    }

    // WebSocket endpoints
    @MessageMapping("/chat.send")
    public void handleSendMessage(@Payload SendMessageRequest request)
            throws ChatNotFoundException, UserNotFoundException {
        try {
            messageService.sendMessage(request);
        } catch (Exception e) {
            log.error("Error saving message", e);
            throw e;
        }
    }
}