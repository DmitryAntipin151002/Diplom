package UserService.service;

import UserService.model.Role;
import UserService.model.RoleName;
import UserService.model.User;
import UserService.repository.RoleRepository;
import UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role defaultRole = roleRepository.findByName(RoleName.PARTICIPANT)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(defaultRole); // Теперь устанавливаем объект Role

        return userRepository.save(user);
    }
}
