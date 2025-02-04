package UserService.service;

import UserService.domain.dto.request.LoginRequest;
import UserService.domain.dto.request.RegistrationRequest;
import UserService.domain.dto.response.AuthResponse;
import UserService.domain.model.Users;
import jakarta.security.auth.message.AuthException;


public interface UserService {
    AuthResponse register(RegistrationRequest request) throws AuthException;
    AuthResponse login(LoginRequest request) throws AuthException;
    Users getAuthenticatedUser(String email) throws AuthException;
}
