package pl.agnieszkajankowska.enauczyciel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.agnieszkajankowska.enauczyciel.model.Role;
import pl.agnieszkajankowska.enauczyciel.model.UserRepository;
import pl.agnieszkajankowska.enauczyciel.security.User;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public void initializeUsersTableWithBasicRecord() {
        if(repository.findAll().isEmpty()) {
            createAdmin();
            createTeacher();
        }
    }

    //tworzenie użytkownika admin
    private void createAdmin() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Role role = Role.ROLE_ADMIN;

        User user = new User();
        user.setLogin("admin");
        user.setPassword(encoder.encode("haslo"));
        user.setRole(role);

        repository.save(user);
    }

    //tworzenie użytkownika teacher
    private void createTeacher() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Role role = Role.ROLE_TEACHER;

        User user = new User();
        user.setLogin("teacher");
        user.setPassword(encoder.encode("haslo"));
        user.setRole(role);

        repository.save(user);
    }
}
