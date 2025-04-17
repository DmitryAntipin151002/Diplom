package UserService.controller;

import UserService.dto.*;
import UserService.exception.*;
import UserService.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatDto> createChat(@RequestBody CreateChatRequest request) {
        return ResponseEntity.ok(chatService.createChat(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatDto>> getUserChats(@PathVariable UUID userId) {
        return ResponseEntity.ok(chatService.getUserChats(userId));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> getChatInfo(
            @PathVariable UUID chatId,
            @RequestParam UUID userId) throws ChatNotFoundException {
        return ResponseEntity.ok(chatService.getChatInfo(chatId, userId));
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