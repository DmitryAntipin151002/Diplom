package UserService.dto;

import lombok.Data;

@Data
public class AttachmentRequest {
    private String fileUrl;
    private String fileName;
    private String fileType;
    private Long fileSize;
}

