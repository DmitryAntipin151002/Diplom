package UserProfileService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_profiles", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "bio")
    private String bio;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "location")
    private String location;


    @Column(name = "sport_type")
    private String sportType;

    @Column(name = "fitness_level")
    private String fitnessLevel;

    // Изменение типов на String
    @Column(name = "goals", columnDefinition = "TEXT")
    private String goals;

    @Column(name = "achievements", columnDefinition = "TEXT")
    private String achievements;

    @Column(name = "personal_records", columnDefinition = "TEXT")
    private String personalRecords;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt;

    @Column(name = "total_activities")
    private Integer totalActivities;

    @Column(name = "total_distance")
    private BigDecimal totalDistance;

    @Column(name = "total_wins")
    private Integer totalWins;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPhoto> photos;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserActivity> activities;

    // @PrePersist для автоматического задания createdAt
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // @PreUpdate для автоматического задания updatedAt
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
