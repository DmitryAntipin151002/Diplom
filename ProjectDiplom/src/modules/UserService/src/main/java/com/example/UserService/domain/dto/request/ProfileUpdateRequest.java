package dto.request;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String fullName;
    private String photoUrl;
    private String interests;
    private String preferredSports;
}
