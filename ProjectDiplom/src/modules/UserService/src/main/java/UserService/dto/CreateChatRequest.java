package UserService.dto;

import UserService.model.ChatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatRequest {
    @NotNull(message = "creatorId is required")
    private UUID creatorId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Type is required")
    private ChatType type;

    private UUID eventId;
    @NotEmpty(message = "Participants list cannot be empty")
    private List<UUID> participantIds;
}