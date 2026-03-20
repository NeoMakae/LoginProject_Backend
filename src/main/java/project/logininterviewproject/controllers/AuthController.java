package project.logininterviewproject.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.logininterviewproject.DTO.LoginRequest;
import project.logininterviewproject.DTO.LoginResponse;
import project.logininterviewproject.DTO.RegisterRequest;
import project.logininterviewproject.DTO.UserResponse;
import project.logininterviewproject.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost"})
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String result = authService.register(request);
            logger.info("User registered successfully for email={}", request.getEmail());

            Map<String, String> response = new HashMap<>();
            response.put("message", "success");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            logger.warn("Registration failed for email={}: {}", request.getEmail(), e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error during registration for email={}", request.getEmail(), e);
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);

            if (response.getUser() == null) {
                logger.warn("Login failed for email={}", request.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            logger.info("Login successful for email={}", request.getEmail());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Unexpected error during login for email={}", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse("Something went wrong. Please try again.", null));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(@RequestParam String email) {
        try {
            return authService.getUser(email).map(user -> ResponseEntity.ok(user)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            logger.error("Unexpected error fetching user for email={}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
