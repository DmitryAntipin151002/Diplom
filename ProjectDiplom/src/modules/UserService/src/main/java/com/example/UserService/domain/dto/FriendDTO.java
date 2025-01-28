package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDTO {
    private Integer id;
    private Integer userId;
    private Integer friendId;
    private LocalDateTime createdAt;
}
