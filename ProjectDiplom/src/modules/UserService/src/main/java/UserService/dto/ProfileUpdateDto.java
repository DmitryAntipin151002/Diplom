package UserService.dto;


import lombok.Data;

@Data
public class ProfileUpdateDto {
    private String firstName;
    private String lastName;
    private String bio;
    private String location;
    private String website;
    private String sportType;
}
