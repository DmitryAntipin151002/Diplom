package UserService.controller;

import UserService.dto.*;
import UserService.exception.*;
import UserService.model.Chat;
import UserService.repository.ChatParticipantRepository;
import UserService.repository.ChatRepository;
import UserService.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    @PostMapping
    public ResponseEntity<ChatDto> createChat(
            @Valid @RequestBody CreateChatRequest request,
            BindingResult result) {

        if (result.hasErrors()) {
            throw new IllegalArgumentException(
                    result.getFieldErrors().stream()
                            .map(e -> e.getField() + ": " + e.getDefaultMessage())
                            .collect(Collectors.joining(", "))
            );
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.createChat(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatDto>> getUserChats(@PathVariable UUID userId) {
        return ResponseEntity.ok(chatService.getUserChats(userId));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> getChatInfo(
            @PathVariable UUID chatId,
            @RequestParam UUID userId) {

        // 1. Проверяем существование чата
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));

        // 3. Возвращаем данные чата
        return   ResponseEntity.ok(chatService.getChatInfo(chatId, userId));
    }

    @PostMapping("/{chatId}/participants")
    public ResponseEntity<Void> addParticipants(
            @PathVariable UUID chatId,
            @RequestBody List<UUID> participantIds,
            @RequestParam UUID requesterId) throws ChatNotFoundException {
        chatService.addParticipants(chatId, participantIds, requesterId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{chatId}/participants/{participantId}")
    public ResponseEntity<Void> removeParticipant(
            @PathVariable UUID chatId,
            @PathVariable UUID participantId,
            @RequestParam UUID requesterId) throws ChatNotFoundException {
        chatService.removeParticipant(chatId, participantId, requesterId);
        return ResponseEntity.ok().build();
    }
}