package AuthenticationService.service.impl;

import AuthenticationService.configuration.AuthenticationServiceProperties;
import AuthenticationService.domain.dto.*;
import AuthenticationService.domain.exception.*;
import AuthenticationService.domain.mapper.UserMapper;
import AuthenticationService.domain.model.Role;
import AuthenticationService.domain.model.Status;
import AuthenticationService.domain.model.User;
import AuthenticationService.domain.model.VerificationCode;
import AuthenticationService.repository.VerificationCodeRepository;
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
import java.util.Optional;
import java.util.Random;

import static AuthenticationService.constants.SecurityConstants.IS_FIRST_ENTER_TOKEN;
import static AuthenticationService.constants.ServiceConstants.*;


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
    private final VerificationCodeRepository verificationCodeRepository;

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
        String code = String.valueOf(random.nextInt(900000) + 100000);
        return userMapper.codeToVerificationCodeDto(code);
    }

    @Override
    @Transactional(noRollbackFor = AttemptCountFailException.class)
    public void checkVerificationCode(VerificationCodeDto verificationCodeDto, User user) {
        // Извлекаем код из DTO
        String codeFromRequest = verificationCodeDto.code();

        // Получаем код из базы данных для данного пользователя
        Optional<VerificationCode> optionalVerificationCode = verificationCodeRepository.findByUserId(user.getId());

        // Проверяем, найден ли код
        if (optionalVerificationCode.isEmpty()) {
            throw new LoginFailException("Verification code not found for user.");
        }

        // Извлекаем код из объекта VerificationCode
        String expectedCode = optionalVerificationCode.get().getCode();

        // Сравниваем коды
        if (!codeFromRequest.equals(expectedCode)) {
            throw new LoginFailException("The code is invalid.");
        }
    }

    @Override
    @Transactional
    public Map<String, String> changePassword(String token, ChangePasswordDto changePasswordDto) {
        // Получаем ID пользователя из токена
        String userId = jwtTokenUtil.getUserId(token); // userId только из токена
        User user = userService.findById(userId);

        // Проверяем старый пароль
        if (!passwordEncoder.matches(changePasswordDto.password(), user.getEncryptedPassword())) {
            throw new PasswordNotMatchesException("The old password is incorrect");
        }

        // Проверяем, что новый пароль отличается от старого
        if (passwordEncoder.matches(changePasswordDto.newPassword(), user.getEncryptedPassword())) {
            throw new NewPasswordMatchesOldPasswordException("The new password must not match the old one");
        }

        // Обновляем пароль и сбрасываем флаг первого входа
        user.setEncryptedPassword(passwordEncoder.encode(changePasswordDto.newPassword()));
        user.setIsFirstEnter(false);

        return Map.of("message", "Password successfully changed");
    }


    @Override
    @Transactional
    public SuccessfulUserRegistrationDto registerUser(UserRegistrationRequestDto registrationInfo) {
        // Проверка уникальности email
        if (userService.existsByEmail(registrationInfo.getEmail())) {
            log.error("Email already exists: {}", registrationInfo.getEmail());
            throw new UserAlreadyExistsException("Email is already taken");
        }


        // Получение роли по ID
        Role role = roleService.findById(registrationInfo.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        // Создание нового пользователя
        User newUser = new User();
        newUser.setEmail(registrationInfo.getEmail());
        newUser.setEncryptedPassword(passwordEncoder.encode(registrationInfo.getPassword()));
        newUser.setIsFirstEnter(true);  // Устанавливаем, что это первый вход
        newUser.setRole(role);  // Устанавливаем роль пользователя

        // Сохраняем пользователя в базе данных
        User savedUser = userService.save(newUser);

        // Генерация DTO для успешной регистрации
        SuccessfulUserRegistrationDto response = new SuccessfulUserRegistrationDto();
        response.setUserId(savedUser.getId().toString());  // Используем ID из сохраненного пользователя
        response.setEmail(savedUser.getEmail());


        return response;
    }
}
 

