package project.logininterviewproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import project.logininterviewproject.DTO.LoginRequest;
import project.logininterviewproject.DTO.RegisterRequest;
import project.logininterviewproject.DTO.UserResponse;
import project.logininterviewproject.data.User;
import project.logininterviewproject.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        logger.info("Register request received: firstName={}, lastName={}, email={}",
                request.getFirstName(), request.getLastName(), request.getEmail());

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        String result = authService.register(user, request.getPassword());
        logger.info("Register result: {}", result);

        return result;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword());
    }

    @GetMapping("/user")
    public UserResponse getUserDetails(@RequestParam String email) {
        User user = authService.getUserDetails(email);
        if (user == null) return null;
        return new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
