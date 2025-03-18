package AuthenticationService.domain.exception;

public class AuthoritiesEmptyException extends RuntimeException {
    public AuthoritiesEmptyException(String message) {
        super(message);
    }
}
