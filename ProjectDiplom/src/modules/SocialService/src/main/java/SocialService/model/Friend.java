package SocialService.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "friend_id", nullable = false)
    private Long friendId;
}