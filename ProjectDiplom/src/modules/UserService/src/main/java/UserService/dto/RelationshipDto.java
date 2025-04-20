package UserService.dto;


import UserService.model.RelationshipStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RelationshipDto {
    private Long id;
    private UUID userId;
    private UUID relatedUserId;
    private String type;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Добавьте связи с сущностями пользователей
    private UserDto user;
    private UserDto relatedUser;
}