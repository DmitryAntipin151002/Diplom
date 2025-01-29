package com.example.UserService.service.Impl;


import com.example.UserService.exception.AuthException;

import com.example.UserService.exception.ResourceNotFoundException;
import com.example.UserService.repository.ProfileRepository;
import com.example.UserService.repository.UserActivityRepository;
import com.example.UserService.repository.UsersRepository;
import com.example.UserService.security.JwtUtil;
import com.example.UserService.service.UserService;
import dto.request.LoginRequest;
import dto.request.RegistrationRequest;
import dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import model.Profile;
import model.UserActivity;
import model.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserActivityRepository activityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email already exists");
        }

        Users user = Users.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);

        // Create profile and activity
        profileRepository.save(Profile.builder().user(user).build());
        activityRepository.save(UserActivity.builder().user(user).build());

        return AuthResponse.builder()
                .email(user.getEmail())
                .accessToken(jwtUtil.generateToken(user))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Invalid credentials");
        }

        return AuthResponse.builder()
                .email(user.getEmail())
                .accessToken(jwtUtil.generateToken(user))
                .build();
    }

    @Override
    public Users getAuthenticatedUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}