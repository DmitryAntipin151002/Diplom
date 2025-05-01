package UserService.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class EditMessageRequest {
    private String newContent;
    private UUID editorId;
}
