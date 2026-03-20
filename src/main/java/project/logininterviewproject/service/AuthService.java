package project.logininterviewproject.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import project.logininterviewproject.DTO.LoginRequest;
import project.logininterviewproject.DTO.LoginResponse;
import project.logininterviewproject.DTO.RegisterRequest;
import project.logininterviewproject.DTO.UserResponse;
import project.logininterviewproject.data.User;
import project.logininterviewproject.repository.UserRepository;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }

        User newUser = new User();
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        userRepository.save(newUser);
        return "User registered successfully!";
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return new LoginResponse("User not found!", null);
        }

        User user = optionalUser.get();
        if (!BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
            return new LoginResponse("Invalid credentials!", null);
        }

        UserResponse userResp = new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail());
        return new LoginResponse("Login successful!", userResp);
    }

    public Optional<UserResponse> getUser(String email) {
        return userRepository.findByEmail(email).map(user -> new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail()));
    }
}
