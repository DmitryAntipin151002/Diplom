package UserProfileService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_activities", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    @Column(name = "activity_type", nullable = false)
    private String activityType;

    @Column(name = "distance")
    private BigDecimal distance;

    @Column(name = "duration")
    private String duration;

    @Column(name = "calories_burned")
    private Integer caloriesBurned;

    @Column(name = "activity_date", nullable = false)
    private LocalDateTime activityDate;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "raw_data", columnDefinition = "jsonb")
    private String rawData;
}

