package com.example.UserService.controller;

import com.example.UserService.service.ProfileService;
import dto.ProfileDTO;
import model.Profile;
import model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<Profile> createOrUpdateProfile(@RequestBody ProfileDTO profileDTO, @RequestAttribute Users user) {
        Profile profile = profileService.createOrUpdateProfile(user, profileDTO);
        return ResponseEntity.ok(profile);
    }

    @GetMapping
    public ResponseEntity<Profile> getProfile(@RequestAttribute Users user) {
        Profile profile = profileService.getProfile(user);
        return ResponseEntity.ok(profile);
    }
}
