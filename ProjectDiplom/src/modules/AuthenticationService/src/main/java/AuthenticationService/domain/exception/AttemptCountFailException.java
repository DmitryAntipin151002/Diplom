package AuthenticationService.domain.exception;

public class AttemptCountFailException extends RuntimeException {
    public AttemptCountFailException(String message) {
        super(message);
    }
}
