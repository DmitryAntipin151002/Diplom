package UserService.dto;

import UserService.model.EventParticipant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipantDto {
    private UUID id;
    private UUID userId;
    private String userName;
    private String userAvatar;
    private String city;
    private EventParticipant.ParticipantStatus status;
    private EventParticipant.ParticipantRole role;
    private LocalDateTime joinedAt;
}
