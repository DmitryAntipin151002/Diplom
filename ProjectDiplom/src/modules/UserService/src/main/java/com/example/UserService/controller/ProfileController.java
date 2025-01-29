package com.example.UserService.controller;


import com.example.UserService.service.ProfileService;
import com.example.UserService.service.UserService;
import dto.request.ProfileUpdateRequest;
import dto.response.ProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import model.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal String email) {
        Users user = userService.getAuthenticatedUser(email);
        return ResponseEntity.ok(profileService.getProfile(user));
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody ProfileUpdateRequest request) {
        Users user = userService.getAuthenticatedUser(email);
        return ResponseEntity.ok(profileService.updateProfile(user, request));
    }
}