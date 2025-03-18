package AuthenticationService.domain.exception;

import static AuthenticationService.constants.ConfigurationConstants.THE_NEW_AND_OLD_PASSWORDS_MUST_BE_DIFFERENT;

public class NewPasswordMatchesOldPasswordException extends RuntimeException {
    public NewPasswordMatchesOldPasswordException() {
        super(THE_NEW_AND_OLD_PASSWORDS_MUST_BE_DIFFERENT);
    }
}
