package UserService.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    private String firstName;
    private String lastName;
    private String bio;
    private String location;
    private String website;
    private String avatarUrl;
    @Column(name = "sport_type")
    private String sportType;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}