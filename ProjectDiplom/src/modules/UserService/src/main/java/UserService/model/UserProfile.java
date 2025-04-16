package UserService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    private UUID userId;

    @OneToOne
    @MapsId // Использует userId как ID и как внешний ключ к User
    @JoinColumn(name = "user_id")
    private User user;

    private String firstName;
    private String lastName;
    private String bio;
    private String location;
    private String website;
    private String avatarUrl;
    private String sportType;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}