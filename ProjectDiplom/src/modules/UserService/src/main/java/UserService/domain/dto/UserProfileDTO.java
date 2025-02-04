package UserService.domain.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    public String fullName;
    public String photoUrl;
    public String interests;
    public String preferredSports;
    public Long userId; // добавлено поле userId
}


