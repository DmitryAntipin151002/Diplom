package AuthenticationService.domain.exception;

public class UserDeletedException extends RuntimeException {
    public UserDeletedException(String message) {
        super(message);
    }
}
