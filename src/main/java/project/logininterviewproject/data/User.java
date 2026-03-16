package project.logininterviewproject.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "app_user") // table name stays
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;               // lowercase

    private String firstName;     // lowercase
    private String lastName;      // lowercase

    @Column(nullable = false, unique = true)
    private String email;         // lowercase

    @Column(nullable = false)
    private String passwordHash;  // lowercase
}