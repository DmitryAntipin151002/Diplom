package UserService.exception;


import java.util.UUID;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(UUID chatId) {
        super("Chat not found with id: " + chatId);
    }

    // Constructor with String message
    public ChatNotFoundException(String message) {
        super(message);
    }
}

