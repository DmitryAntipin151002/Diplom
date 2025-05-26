package AuthenticationService.service.impl;

import AuthenticationService.domain.model.Role;
import AuthenticationService.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }
}

