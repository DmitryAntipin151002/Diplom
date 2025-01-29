package com.example.UserService.service;


import dto.request.LoginRequest;
import dto.request.RegistrationRequest;
import dto.response.AuthResponse;
import model.Users;


public interface UserService {
    AuthResponse register(RegistrationRequest request);
    AuthResponse login(LoginRequest request);
    Users getAuthenticatedUser(String email);
}
