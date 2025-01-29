package com.example.UserService.service.Impl;


import com.example.UserService.exception.ResourceNotFoundException;
import com.example.UserService.repository.ProfileRepository;
import com.example.UserService.service.ProfileService;
import dto.request.ProfileUpdateRequest;
import dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import mapper.ProfileMapper;
import model.Profile;
import model.Users;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public ProfileResponse getProfile(Users user) {
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
        return profileMapper.toProfileResponse(profile);
    }

    @Override
    public ProfileResponse updateProfile(Users user, ProfileUpdateRequest request) {
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        profileMapper.updateProfileFromRequest(request, profile);
        Profile updatedProfile = profileRepository.save(profile);

        return profileMapper.toProfileResponse(updatedProfile);
    }
}