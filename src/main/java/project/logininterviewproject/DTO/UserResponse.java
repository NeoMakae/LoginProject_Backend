package project.logininterviewproject.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponse {

    private String firstName;

    private String lastName;

    private String email;

    public UserResponse(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
