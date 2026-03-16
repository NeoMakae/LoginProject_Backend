package project.logininterviewproject.service;

import org.springframework.stereotype.Service;
import project.logininterviewproject.data.User;
import project.logininterviewproject.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(User user, String plainPassword) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Username already exists!";
        }
        user.setPasswordHash(BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "User not found!";
        if (BCrypt.checkpw(password, user.getPasswordHash())) {
            return "Login successful!";
        } else {
            return "Invalid credentials!";
        }
    }

    public User getUserDetails(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
