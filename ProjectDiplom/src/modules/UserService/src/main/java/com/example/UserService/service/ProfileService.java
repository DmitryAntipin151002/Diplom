package com.example.UserService.service;


import dto.request.ProfileUpdateRequest;
import dto.response.ProfileResponse;
import model.Users;




public interface ProfileService {
    ProfileResponse getProfile(Users user);
    ProfileResponse updateProfile(Users user, ProfileUpdateRequest request);
}
