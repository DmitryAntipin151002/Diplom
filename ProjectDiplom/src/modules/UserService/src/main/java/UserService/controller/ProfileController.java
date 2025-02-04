package UserService.controller;

import UserService.domain.dto.request.ProfileUpdateRequest;
import UserService.domain.dto.response.UserProfileResponse;
import UserService.domain.model.Users;
import UserService.service.ProfileService;
import UserService.service.UserService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final  UserService userService;

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal String email) throws AuthException {
        Users user = userService.getAuthenticatedUser(email);
        return ResponseEntity.ok(profileService.getProfile(user));
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal String email,
            @Valid @RequestBody ProfileUpdateRequest request) throws AuthException {
        Users user = userService.getAuthenticatedUser(email);
        return ResponseEntity.ok(profileService.updateProfile(user, request));
    }
}