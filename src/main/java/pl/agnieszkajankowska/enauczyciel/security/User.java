package pl.agnieszkajankowska.enauczyciel.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.agnieszkajankowska.enauczyciel.model.Role;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
