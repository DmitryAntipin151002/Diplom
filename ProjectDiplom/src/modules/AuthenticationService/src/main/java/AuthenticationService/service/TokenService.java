package AuthenticationService.service;

import AuthenticationService.domain.dto.JwtRefreshTokenDto;
import AuthenticationService.domain.dto.JwtToken;
import AuthenticationService.domain.dto.VerificationCodeDto;

public interface TokenService {

    /**
     * Генерация токенов доступа исходя из типа токена, переданного в заголовке запроса. Перед генерацией пользователь
     * проходит через ряд фильтров по статусу.
     *
     * @param userId              uuid пользователя
     * @param token               токен, переданный в заголовке запроса
     * @param verificationCodeDto код верификации, завершающий этап двухфакторной аутентификации
     * @return RecoveryTokenDto - если в @param token передается токен для восстановления (RECOVERY);
     * IsFirstEnterTokenDto - если в @param token передается preAuthorizationToken и это первый вход в систему;
     * AccessAndRefreshTokensDto - если в @param token передается preAuthorizationToken и это повторный вход в систему;
     */
    JwtToken getTokens(String userId, String token, VerificationCodeDto verificationCodeDto);


}
