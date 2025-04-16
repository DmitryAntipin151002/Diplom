package UserService.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "relationship_types")
public class RelationshipTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}
