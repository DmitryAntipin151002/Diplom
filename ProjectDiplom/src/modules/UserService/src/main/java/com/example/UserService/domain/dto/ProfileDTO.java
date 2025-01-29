package dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProfileDTO {
    private String fullName;
    private String photoUrl;
    private String interests;
    private String preferredSports;
}

