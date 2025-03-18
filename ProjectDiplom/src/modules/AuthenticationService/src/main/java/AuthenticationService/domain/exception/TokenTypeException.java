package AuthenticationService.domain.exception;

import static AuthenticationService.constants.ConfigurationConstants.TOKEN_TYPE_IS_INCORRECT_FOR_THIS_OPERATION;

public class TokenTypeException extends RuntimeException {
    public TokenTypeException() {
        super(TOKEN_TYPE_IS_INCORRECT_FOR_THIS_OPERATION);
    }
}
