package AuthenticationService.controller.impl;

import AuthenticationService.controller.AuthorityController;
import AuthenticationService.domain.dto.AuthorityDto;
import AuthenticationService.domain.dto.UserDto;
import AuthenticationService.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/v1/authentication-service")
@RestController
public class AuthorityControllerImpl implements AuthorityController {
    private final AuthorityService authorityService;

    @Override
    @GetMapping("/get-authorities")
    public ResponseEntity<List<AuthorityDto>> getAllAuthorities() {
        return ResponseEntity.ok(authorityService.getAllAuthorities());
    }

    @Override
    @PostMapping("/add-user")
    public ResponseEntity<Void> addUser(@RequestBody UserDto userDto) {
        authorityService.addUser(userDto);
        return ResponseEntity.ok().build();
    }
}
