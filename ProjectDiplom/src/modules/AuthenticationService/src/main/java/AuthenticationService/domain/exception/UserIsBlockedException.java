package AuthenticationService.domain.exception;

public class UserIsBlockedException extends RuntimeException {
    public UserIsBlockedException(String message) {
        super(message);
    }
}
