package UserService.dto;


import lombok.Data;

@Data
public class UserStatsDto {
    private Long eventCount;
    private Long friendsCount;
    private Long notificationCount;
}
