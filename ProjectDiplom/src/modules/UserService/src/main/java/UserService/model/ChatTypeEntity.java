package UserService.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chat_types")
public class ChatTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Column(length = 255)
    private String description;
}