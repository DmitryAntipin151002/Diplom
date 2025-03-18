package AuthenticationService.service.impl;

import AuthenticationService.configuration.AuthenticationServiceProperties;
import AuthenticationService.domain.dto.*;
import AuthenticationService.domain.exception.*;
import AuthenticationService.domain.mapper.UserMapper;
import AuthenticationService.domain.model.Role;
import AuthenticationService.domain.model.Status;
import AuthenticationService.domain.model.User;
import AuthenticationService.service.AuthenticationService;
import AuthenticationService.service.UserService;
import AuthenticationService.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static AuthenticationService.constants.SecurityConstants.IS_FIRST_ENTER_TOKEN;
import static AuthenticationService.constants.ServiceConstants.*;
import static AuthenticationService.domain.enums.StatusName.BLOCKED;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final Random random = new Random();
    private final AuthenticationServiceProperties authenticationServiceProperties;

    @Override
    @Transactional
    public SuccessfulUserLoginDto authenticate(UserLoginRequestDto loginInfo) {
        log.info(SEARCH_FOR_A_USER_WITH_EMAIL, loginInfo.email());
        String email = loginInfo.email();
        String password = loginInfo.password();

        User user = userService.findByEmail(email);

        String encryptedPasswordFromDB = user.getEncryptedPassword();
        if (passwordEncoder.matches(password, encryptedPasswordFromDB)) {
            log.info(SEARCH_FOR_EMAIL_WAS_SUCCESSFUL, email);
            String preAuthToken = jwtTokenUtil.createPreAuthorizedToken();
            return userMapper.userToSuccessfulUserLoginDto(user, preAuthToken);
        } else {
            log.error(THE_TRANSMITTED_DATA_WAS_INCORRECT_FOR_EMAIL, email);
            throw new LoginFailException(PASSWORD_FOR_EMAIL_EXCEPTION + email);
        }
    }

    @Override
    @Transactional
    public VerificationCodeDto generateVerificationCode(String userId) {
        // Убираем Redis вызовы. Логика может быть изменена для использования других механизмов.
        String code = String.valueOf(random.nextInt(900000) + 100000);
        return userMapper.codeToVerificationCodeDto(code);
    }

    @Override
    @Transactional(noRollbackFor = AttemptCountFailException.class)
    public void checkVerificationCode(VerificationCodeDto verificationCodeDto, User user) {
        String userId = user.getId().toString();

        // Убираем Redis вызовы. Логика проверки кода и попыток будет другим.
        String codeFromRequest = verificationCodeDto.code();

        // Условие для сравнения кода
        if (!"someCode".equals(codeFromRequest)) {  // Примерный код для проверки
            throw new LoginFailException("The code is invalid.");
        }
    }

    @Override
    @Transactional
    public Map<String, String> changePassword(String token, ChangePasswordDto changePasswordDto) {
        try {
            String userId = jwtTokenUtil.getUserId(token);
            User user = userService.findById(userId);

            if (!passwordEncoder.matches(changePasswordDto.password(), user.getEncryptedPassword())) {
                log.error(THE_PASSWORD_DOES_NOT_MATCH_THE_PASSWORD_FROM_THE_DB);
                throw new PasswordNotMatchesException();
            }

            if (passwordEncoder.matches(changePasswordDto.newPassword(), user.getEncryptedPassword())) {
                log.error(THE_NEW_PASSWORD_MATCHES_THE_OLD_PASSWORD_FROM_THE_DB);
                throw new NewPasswordMatchesOldPasswordException();
            }

            user.setEncryptedPassword(passwordEncoder.encode(changePasswordDto.newPassword()));
            user.setIsFirstEnter(false);

            String tokenType = jwtTokenUtil.getTokenType(token);
            if (tokenType.equals(IS_FIRST_ENTER_TOKEN)) {
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", jwtTokenUtil.createAccessToken(userId));
                tokens.put("refreshToken", jwtTokenUtil.createRefreshToken(userId));
                return tokens;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error changing password: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public SuccessfulUserRegistrationDto registerUser(UserRegistrationRequestDto registrationInfo) {
        // Проверка уникальности email
        if (userService.existsByEmail(registrationInfo.getEmail())) {
            log.error("Email already exists: {}", registrationInfo.getEmail());
            throw new UserAlreadyExistsException("Email is already taken");
        }

        // Проверка уникальности телефона
        if (userService.existsByPhone(registrationInfo.getPhone())) {
            log.error("Phone number already exists: {}", registrationInfo.getPhone());
            throw new UserAlreadyExistsException("Phone number is already taken");
        }

        // Получение роли по ID
        Role role = roleService.findById(registrationInfo.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        // Создание нового пользователя
        User newUser = new User();
        newUser.setEmail(registrationInfo.getEmail());
        newUser.setPhoneNumber(registrationInfo.getPhone());
        newUser.setEncryptedPassword(passwordEncoder.encode(registrationInfo.getPassword()));
        newUser.setIsFirstEnter(true);  // Устанавливаем, что это первый вход
        newUser.setRole(role);  // Устанавливаем роль пользователя

        // Сохраняем пользователя в базе данных
        User savedUser = userService.save(newUser);

        // Генерация DTO для успешной регистрации
        SuccessfulUserRegistrationDto response = new SuccessfulUserRegistrationDto();
        response.setUserId(savedUser.getId().toString());  // Используем ID из сохраненного пользователя
        response.setEmail(savedUser.getEmail());
        response.setPhone(savedUser.getPhoneNumber());  // Убедимся, что используем правильное имя поля

        return response;
    }
}
 

