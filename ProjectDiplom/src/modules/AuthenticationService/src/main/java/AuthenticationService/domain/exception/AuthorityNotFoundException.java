package AuthenticationService.domain.exception;

public class AuthorityNotFoundException extends RuntimeException {
    public AuthorityNotFoundException(String message) {
        super(message);
    }
}
