package UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponseDto {
    private UUID id;
    private String photoUrl;
    private String description;
    private boolean isProfilePhoto;
}