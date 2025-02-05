package UserService.service.Impl;

import UserService.domain.dto.request.LoginRequest;
import UserService.domain.dto.request.RegistrationRequest;
import UserService.domain.dto.response.AuthResponse;
import UserService.domain.model.UserActivity;
import UserService.domain.model.UserProfile;
import UserService.domain.model.Users;
import UserService.exception.ResourceNotFoundException;
import UserService.repository.ProfileRepository;
import UserService.repository.UserActivityRepository;
import UserService.repository.UsersRepository;
import UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserActivityRepository activityRepository;

    @Override
    public AuthResponse register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Users user = Users.builder()
                .email(request.getEmail())
                .passwordHash(request.getPassword()) // ❌ Убрали шифрование пароля
                .build();

        user = userRepository.save(user);

        profileRepository.save(UserProfile.builder().user(user).build());
        activityRepository.save(UserActivity.builder().user(user).build());

        return AuthResponse.builder()
                .email(user.getEmail())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!request.getPassword().equals(user.getPasswordHash())) { // ❌ Убрали проверку с PasswordEncoder
            throw new IllegalArgumentException("Invalid credentials");
        }

        return AuthResponse.builder()
                .email(user.getEmail())
                .build();
    }

    @Override
    public Users getAuthenticatedUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
    }
}
