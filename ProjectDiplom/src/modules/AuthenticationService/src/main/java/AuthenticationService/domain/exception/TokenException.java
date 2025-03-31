package AuthenticationService.domain.exception;

import static AuthenticationService.constants.ConfigurationConstants.THE_TOKEN_HAS_EXPIRED_OR_THE_TOKEN_IS_NOT_VALID;

public class TokenException extends RuntimeException {
    public TokenException(String tokenIsNullOrEmpty) {
        super(THE_TOKEN_HAS_EXPIRED_OR_THE_TOKEN_IS_NOT_VALID);
    }
}
