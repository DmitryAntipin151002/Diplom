package UserProfileService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_photos", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPhoto {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile userProfile;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "is_main")
    private Boolean isMain;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name = "description")
    private String description;
}

