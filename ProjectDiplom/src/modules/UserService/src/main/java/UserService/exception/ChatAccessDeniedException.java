package UserService.exception;

import java.util.UUID;

public class ChatAccessDeniedException extends RuntimeException {
    public ChatAccessDeniedException(UUID userId, UUID chatId, String reason) {
        super(String.format("User %s denied access to chat %s: %s", userId, chatId, reason));
    }

    public ChatAccessDeniedException(String message) {
        super(message);
    }
}
