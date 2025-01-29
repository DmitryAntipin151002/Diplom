package com.example.UserService.service;

import com.example.UserService.repository.UsersRepository;
import dto.UserLoginDTO;
import dto.UserRegistrationDTO;
import model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users registerUser(UserRegistrationDTO registrationDTO) {
        Users user = new Users();
        user.setEmail(registrationDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registrationDTO.getPassword()));
        return usersRepository.save(user);
    }

    public Users authenticateUser(UserLoginDTO loginDTO) {
        Users user = usersRepository.findByEmail(loginDTO.getEmail());
        if (user != null && passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            return user;
        }
        return null;
    }
}
