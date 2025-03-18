package AuthenticationService.service.impl;

import AuthenticationService.domain.dto.EmailAndPhoneDto;
import AuthenticationService.domain.dto.JwtRecoveryTokenDto;
import AuthenticationService.domain.dto.UpdatePasswordDto;
import AuthenticationService.domain.exception.NewPasswordMatchesOldPasswordException;
import AuthenticationService.domain.model.User;
import AuthenticationService.service.UserRecoveryService;
import AuthenticationService.service.UserService;
import AuthenticationService.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static AuthenticationService.constants.ServiceConstants.PASSWORD_CHANGED_SUCCESSFULLY;
import static AuthenticationService.constants.ServiceConstants.THE_NEW_PASSWORD_MATCHES_THE_OLD_PASSWORD_FROM_THE_DB;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRecoveryServiceImpl implements UserRecoveryService {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public JwtRecoveryTokenDto userExistenceCheck(EmailAndPhoneDto emailAndPhoneDto) {
        String email = emailAndPhoneDto.email();
        String phoneNumber = emailAndPhoneDto.phoneNumber();
        User user = userService.findUserByEmailAndPhoneNumber(email, phoneNumber);
        String userId = user.getId().toString();
        return new JwtRecoveryTokenDto(userId, jwtTokenUtil.createRecoveryToken(userId));
    }

    @Override
    @Transactional
    public void updatePassword(String token, UpdatePasswordDto updatePasswordDto) {
        String newPassword = updatePasswordDto.newPassword();
        String userId = jwtTokenUtil.getUserId(token);
        User user = userService.findById(userId);
        String oldEncryptedPassword = user.getEncryptedPassword();

        if (passwordEncoder.matches(newPassword, oldEncryptedPassword)) {
            log.error(THE_NEW_PASSWORD_MATCHES_THE_OLD_PASSWORD_FROM_THE_DB);
            throw new NewPasswordMatchesOldPasswordException();
        }

        user.setEncryptedPassword(passwordEncoder.encode(newPassword));
        log.info(PASSWORD_CHANGED_SUCCESSFULLY);
    }
}
