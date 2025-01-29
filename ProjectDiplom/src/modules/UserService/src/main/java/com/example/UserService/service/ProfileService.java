package com.example.UserService.service;

import com.example.UserService.repository.ProfileRepository;
import dto.ProfileDTO;
import model.Profile;
import model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public Profile createOrUpdateProfile(Users user, ProfileDTO profileDTO) {
        Profile profile = profileRepository.findByUser(user);
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
        }
        profile.setFullName(profileDTO.getFullName());
        profile.setPhotoUrl(profileDTO.getPhotoUrl());
        profile.setInterests(profileDTO.getInterests());
        profile.setPreferredSports(profileDTO.getPreferredSports());
        return profileRepository.save(profile);
    }

    public Profile getProfile(Users user) {
        return profileRepository.findByUser(user);
    }
}
