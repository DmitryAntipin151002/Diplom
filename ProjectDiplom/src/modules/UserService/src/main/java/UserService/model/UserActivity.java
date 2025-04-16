package UserService.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_activities")
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String activityType;

    private Double distance;
    private String duration;
    private Integer caloriesBurned;

    @Column(nullable = false)
    private LocalDateTime activityDate;

    private String externalId;
    private String rawData;

    @CreationTimestamp
    private LocalDateTime createdAt;
}