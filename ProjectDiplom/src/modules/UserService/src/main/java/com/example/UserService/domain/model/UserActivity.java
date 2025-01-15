package model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "user_activity", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserActivity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name= "events_created")
    Integer eventsCreated;

    @Column(name="events_participated")
    Integer eventsParticipated;

    @Column(name="last_active")
    Date lastActive;

}
