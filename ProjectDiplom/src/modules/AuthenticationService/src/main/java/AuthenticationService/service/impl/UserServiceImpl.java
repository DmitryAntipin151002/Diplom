package AuthenticationService.service.impl;

import AuthenticationService.domain.enums.StatusName;
import AuthenticationService.domain.exception.UserNotFoundException;
import AuthenticationService.domain.exception.StatusNotFoundException;  // добавил исключение
import AuthenticationService.domain.model.Status;
import AuthenticationService.domain.model.User;
import AuthenticationService.repository.StatusRepository;
import AuthenticationService.repository.UserRepository;
import AuthenticationService.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;  // добавил для проверки пустых строк

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static AuthenticationService.constants.ServiceConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("User with email {} not found", email);  // улучшил логирование
            return new UserNotFoundException("User with email " + email);
        });
    }

    @Override
    public User findById(String id) {
        if (StringUtils.isEmpty(id)) {
            log.error("ID cannot be null or empty");
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", id);
            throw new UserNotFoundException("Invalid UUID format: " + id);
        }
        return userRepository.findById(uuid).orElseThrow(() -> {
            log.error("User with ID {} not found", id);
            return new UserNotFoundException("User with ID " + id);
        });
    }

    @Override
    public User findUserByEmailAndPhoneNumber(String email, String phoneNumber) {
        return userRepository.findUserByEmailAndPhoneNumber(email, phoneNumber).orElseThrow(() -> {
            log.error("User with email {} and phone number {} not found", email, phoneNumber);  // улучшил логирование
            return new UserNotFoundException("User with email " + email + " and phone number " + phoneNumber + " not found");
        });
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhoneNumber(phone);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        log.debug("Attempting to find status for: {}", StatusName.ACTIVE);

        Optional<Status> statusOpt = statusRepository.findByName(StatusName.ACTIVE);

        if (statusOpt.isPresent()) {
            log.debug("Found status: {}", statusOpt.get());
        } else {
            log.warn("Status '{}' not found", StatusName.ACTIVE);
        }

        user.setStatus(statusOpt.orElseThrow(() -> new StatusNotFoundException("Status ACTIVE not found")));
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public void updateUserStatusAfterLogin(User user) {
        user.setLastLogin(LocalDateTime.now());
        user.setStatus(statusRepository.findByName(StatusName.ACTIVE)
                .orElseThrow(() -> new StatusNotFoundException("Status ACTIVE not found")));  // уточнил выброс исключения
        userRepository.save(user);
    }
}
