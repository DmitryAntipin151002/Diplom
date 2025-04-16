package UserService.controller;

import UserService.dto.RelationshipDto;
import UserService.model.RelationshipStatus;
import UserService.model.RelationshipType;
import UserService.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/relationships")
@RequiredArgsConstructor
public class RelationshipController {

    private final RelationshipService relationshipService;

    /**
     * Получить все отношения пользователя определенного типа
     * @param userId ID пользователя
     * @param type Тип отношений (FRIEND, FOLLOW, BLOCK)
     * @return Список отношений
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RelationshipDto>> getUserRelationships(
            @PathVariable UUID userId,
            @RequestParam String type) { // Изменили на String
        List<RelationshipDto> relationships = relationshipService.getUserRelationships(userId, type);
        return ResponseEntity.ok(relationships);
    }

    /**
     * Создать новое отношение между пользователями
     * @param userId ID основного пользователя
     * @param relatedUserId ID связанного пользователя
     * @param type Тип создаваемого отношения
     * @return Созданное отношение
     */
    @PostMapping
    public ResponseEntity<RelationshipDto> createRelationship(
            @RequestParam UUID userId,
            @RequestParam UUID relatedUserId,
            @RequestParam String type) { // Изменили на String
        RelationshipDto relationship = relationshipService.createRelationship(userId, relatedUserId, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(relationship);
    }

    /**
     * Обновить статус отношения
     * @param relationshipId ID отношения
     * @param status Новый статус (PENDING, ACCEPTED, DECLINED, BLOCKED)
     * @return Обновленное отношение
     */
    @PatchMapping("/{relationshipId}/status")
    public ResponseEntity<RelationshipDto> updateRelationshipStatus(
            @PathVariable Long relationshipId,
            @RequestParam String status) { // Изменили на String
        RelationshipDto updated = relationshipService.updateStatus(relationshipId, status);
        return ResponseEntity.ok(updated);
    }

    /**
     * Получить входящие запросы на дружбу
     * @param userId ID пользователя
     * @return Список входящих запросов
     */
    @GetMapping("/user/{userId}/pending-requests")
    public ResponseEntity<List<RelationshipDto>> getPendingFriendRequests(
            @PathVariable UUID userId) {
        List<RelationshipDto> requests = relationshipService.getUserRelationships(
                        userId,
                        "FRIEND")  // Используем строковый параметр вместо enum
                .stream()
                .filter(r -> "PENDING".equals(r.getStatus()))  // Сравниваем строки
                .toList();
        return ResponseEntity.ok(requests);
    }

    /**
     * Удалить отношение
     * @param relationshipId ID отношения
     * @return Пустой ответ со статусом 204
     */
    @DeleteMapping("/{relationshipId}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable Long relationshipId) {
        relationshipService.deleteRelationship(relationshipId);
        return ResponseEntity.noContent().build();
    }
}