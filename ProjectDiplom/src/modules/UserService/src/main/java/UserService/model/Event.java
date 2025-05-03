// Event.java
package UserService.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;
    private LocalDateTime startTime;
    private String sportType;
    private LocalDateTime endTime;
    private String location;

    @ManyToOne
    @JoinColumn(name = "status", referencedColumnName = "id")
    private EventStatus status;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventParticipant> participants;



}

