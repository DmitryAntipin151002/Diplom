package AuthenticationService.service.impl;

import AuthenticationService.domain.dto.*;
import AuthenticationService.domain.exception.BadRefreshTokenException;
import AuthenticationService.domain.exception.TokenMismatchException;
import AuthenticationService.domain.exception.TokenTypeException;
import AuthenticationService.domain.model.User;
import AuthenticationService.filter.UserStatusFilter;
import AuthenticationService.service.AuthenticationService;
import AuthenticationService.service.TokenService;
import AuthenticationService.service.UserService;
import AuthenticationService.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static AuthenticationService.constants.SecurityConstants.*;
import static AuthenticationService.constants.ServiceConstants.JWT_TOKEN_UPDATED_SUCCESSFULLY;
import static AuthenticationService.constants.ServiceConstants.USER_ID_WITH_THIS_REFRESH_TOKEN_NOT_FOUND_USER_ID_EXCEPTION;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserStatusFilter userStatusFilter;

    @Override
    public JwtToken getTokens(String userId, String token, VerificationCodeDto verificationCodeDto) {
        String tokenType = jwtTokenUtil.getTokenType(token);
        switch (tokenType) {
            case RECOVERY -> {
                return getRecoveryToken(userId);
            }
            case PRE_AUTHORIZE -> {
                User user = userService.findById(userId);
                userStatusFilter.filter(user);
                authenticationService.checkVerificationCode(verificationCodeDto, user);
                return user.getIsFirstEnter() ? getIsFirstEnterToken(userId) : getAccessAndRefreshTokens(user);
            }
            default -> {
                log.error("Выполнение операции c токеном такого типа запрещенно: {}", tokenType);
                throw new TokenTypeException();
            }
        }
    }



    private JwtRecoveryTokenDto getRecoveryToken(String userId) {
        String recoveryToken = jwtTokenUtil.createRecoveryToken(userId);
        return new JwtRecoveryTokenDto(userId, recoveryToken);
    }

    private JwtIsFirstEnterTokenDto getIsFirstEnterToken(String userId) {
        String isFirstEnterToken = jwtTokenUtil.createIsFirstEnterToken(userId);
        return new JwtIsFirstEnterTokenDto(isFirstEnterToken);
    }

    private SuccessfulUserAuthenticationDto getAccessAndRefreshTokens(User user) {
        String userId = user.getId().toString();
        String accessToken = jwtTokenUtil.createAccessToken(userId);
        String refreshToken = jwtTokenUtil.createRefreshToken(userId);
        return new SuccessfulUserAuthenticationDto(accessToken, refreshToken, user.getStatus().getId());
    }
}
