package UserService.exception;

public class ChatTypeNotFoundException extends RuntimeException {
    public ChatTypeNotFoundException(String code) {
        super("Chat type not found with code: " + code);
    }
}