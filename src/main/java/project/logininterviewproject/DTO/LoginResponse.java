package project.logininterviewproject.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String message;
    private UserResponse user;

    public LoginResponse(String message, UserResponse user) {
        this.message = message;
        this.user = user;
    }
}