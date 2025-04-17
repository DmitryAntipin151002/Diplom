package UserService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class AttachmentDTO {
    private String fileUrl;
    private String fileName;
    private String fileType;
}

