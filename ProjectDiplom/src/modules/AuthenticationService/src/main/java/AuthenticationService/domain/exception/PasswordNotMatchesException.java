package AuthenticationService.domain.exception;

import static AuthenticationService.constants.ServiceConstants.THE_PASSWORD_DOES_NOT_MATCH_THE_PASSWORD_FROM_THE_DB;

public class PasswordNotMatchesException extends RuntimeException {
    public PasswordNotMatchesException(String theOldPasswordIsIncorrect) {
        super(THE_PASSWORD_DOES_NOT_MATCH_THE_PASSWORD_FROM_THE_DB);
    }
}
