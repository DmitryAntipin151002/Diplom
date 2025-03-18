package AuthenticationService.domain.model;

import AuthenticationService.domain.enums.RoleName;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ROLE")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "NAME")
    @Enumerated(EnumType.STRING)
    RoleName role;
}
