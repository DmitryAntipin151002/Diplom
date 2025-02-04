package UserService.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
   public Long profileId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    public Users user;

    @Column(name = "full_name")
    public String fullName;

    @Column(name = "photo_url")
    public String photoUrl;

    @Column(name = "interests")
    public String interests;

    @Column(name = "preferred_sports")
    public String preferredSports;
}
