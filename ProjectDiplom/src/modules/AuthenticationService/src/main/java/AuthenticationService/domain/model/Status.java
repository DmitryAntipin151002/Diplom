package AuthenticationService.domain.model;

import AuthenticationService.domain.enums.StatusName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Table(name = "STATUS")
@Accessors(chain = true)
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "NAME")
    @Enumerated(EnumType.STRING)
    StatusName name;
}
