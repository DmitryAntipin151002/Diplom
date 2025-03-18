package AuthenticationService.service.impl;

import AuthenticationService.domain.dto.AuthorityDto;
import AuthenticationService.domain.dto.UserDto;
import AuthenticationService.domain.exception.AuthorityNotFoundException;
import AuthenticationService.domain.exception.RoleNotFoundException;
import AuthenticationService.domain.exception.StatusNotFoundException;
import AuthenticationService.domain.exception.UserAlreadyExistsException;
import AuthenticationService.domain.mapper.AuthorityMapper;
import AuthenticationService.domain.mapper.UserMapper;
import AuthenticationService.domain.model.Authority;
import AuthenticationService.domain.model.User;
import AuthenticationService.repository.AuthorityRepository;
import AuthenticationService.repository.RoleRepository;
import AuthenticationService.repository.StatusRepository;
import AuthenticationService.repository.UserRepository;
import AuthenticationService.service.AuthorityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static AuthenticationService.constants.ConfigurationConstants.NO_VALID_AUTHORITIES;
import static AuthenticationService.constants.ConfigurationConstants.USER_WITH_SUCH_ID_ALREADY_EXISTS;
import static java.util.function.Predicate.not;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final AuthorityMapper authorityMapper;

    public List<AuthorityDto> getAllAuthorities() {
        log.info("Search all available authorities");
        List<Authority> authorities = authorityRepository.findAll();
        return authorities.stream().map(authorityMapper::map).toList();
    }

    @Transactional
    public void addUser(UserDto userDto) {
        if (userRepository.existsById(userDto.id())) {
            log.error(USER_WITH_SUCH_ID_ALREADY_EXISTS);
            throw new UserAlreadyExistsException(USER_WITH_SUCH_ID_ALREADY_EXISTS);
        }

        User user = userMapper.map(userDto);
        log.info("Saving a user with id {} and his authorities to the database", user.getId());
        user.setStatus(statusRepository.findById(1L).orElseThrow(() -> new StatusNotFoundException("Status not found")));
        user.setRole(roleRepository.findById(2L).orElseThrow(() -> new RoleNotFoundException("Role not found")));
        user.setIsFirstEnter(true);

        Set<Authority> authorities = Optional.of(authorityRepository.findByIdIn(userDto.authorityIds()))
                .filter(not(Set::isEmpty))
                .orElseThrow(() -> {
                    log.error(NO_VALID_AUTHORITIES);
                    return new AuthorityNotFoundException(NO_VALID_AUTHORITIES);
                });

        user.setAuthorities(authorities);
        userRepository.save(user);
    }
}
