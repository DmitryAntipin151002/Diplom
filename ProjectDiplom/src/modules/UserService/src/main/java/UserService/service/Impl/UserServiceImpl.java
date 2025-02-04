package UserService.service.Impl;

import UserService.domain.dto.request.LoginRequest;
import UserService.domain.dto.request.RegistrationRequest;
import UserService.domain.model.UserActivity;
import UserService.domain.model.Users;
import UserService.exception.ResourceNotFoundException;
import UserService.domain.dto.response.AuthResponse;

import UserService.domain.model.UserProfile;
import UserService.repository.ProfileRepository;
import UserService.repository.UserActivityRepository;
import UserService.repository.UsersRepository;
import UserService.service.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserActivityRepository activityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegistrationRequest request) throws AuthException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email already exists");
        }

        Users user = Users.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);

        // Create profile and activity
        profileRepository.save(UserProfile.builder().user(user).build());
        activityRepository.save(UserActivity.builder().user(user).build());

        return AuthResponse.builder()
                .email(user.getEmail())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) throws AuthException {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Invalid credentials");
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
}
