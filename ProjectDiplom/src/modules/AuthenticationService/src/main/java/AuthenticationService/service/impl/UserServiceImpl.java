package AuthenticationService.service.impl;

import AuthenticationService.domain.exception.UserNotFoundException;
import AuthenticationService.domain.model.User;
import AuthenticationService.repository.UserRepository;
import AuthenticationService.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static AuthenticationService.constants.ServiceConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.error(USER_WITH_EMAIL_WAS_NOT_FOUND, email);
            return new UserNotFoundException(EMAIL + email);
        });
    }

    @Override
    public User findById(String id) {
        UUID uuid = UUID.fromString(id);
        return userRepository.findById(uuid).orElseThrow(() -> {
            log.error(USER_WITH_ID_WAS_NOT_FOUND, id);
            return new UserNotFoundException(ID + id);
        });
    }

    @Override
    public User findUserByEmailAndPhoneNumber(String email, String phoneNumber) {
        return userRepository.findUserByEmailAndPhoneNumber(email, phoneNumber).orElseThrow(() -> {
            log.error(USER_WITH_EMAIL_AND_PHONE_NUMBER_NOT_FOUND, email, phoneNumber);
            return new UserNotFoundException(String.format
                    (USER_WITH_EMAIL_AND_PHONE_NUMBER_NOT_FOUND_EXCEPTION, email, phoneNumber));
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
        return userRepository.save(user);
    }
}
