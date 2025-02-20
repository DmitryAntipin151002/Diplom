package SocialService.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}