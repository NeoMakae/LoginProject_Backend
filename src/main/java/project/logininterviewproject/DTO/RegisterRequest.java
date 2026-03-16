package project.logininterviewproject.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}
