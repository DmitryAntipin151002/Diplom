package dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {
    private Integer profileId;
    private Integer userId;
    private String fullName;
    private String photoUrl;
    private String interests;
    private String preferredSports;

}

