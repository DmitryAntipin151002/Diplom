package UserService.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContentSecurityPolicyController {

    @RequestMapping("/content-security-policy")
    public String getCSP() {
        return "Content-Security-Policy: connect-src 'self' ws://localhost:8083";
    }
}