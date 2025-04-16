package UserService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "event_participants")
public class EventParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)
    private ParticipantStatus status = ParticipantStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private ParticipantRole role = ParticipantRole.PARTICIPANT;

    public enum ParticipantStatus {
        PENDING, JOINED, REJECTED
    }

    public enum ParticipantRole {
        ORGANIZER, PARTICIPANT
    }
}