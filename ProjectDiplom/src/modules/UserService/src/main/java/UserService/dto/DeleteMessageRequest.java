package UserService.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DeleteMessageRequest {
    private UUID deleterId;
}