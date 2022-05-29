package pl.agnieszkajankowska.enauczyciel.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.agnieszkajankowska.enauczyciel.model.Role;
import pl.agnieszkajankowska.enauczyciel.model.UserRepository;
import pl.agnieszkajankowska.enauczyciel.security.User;

import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repository;

    @Test
    void shouldReturnInstanceOfUserClassAfterAddingToDB() {
        //given
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Role role = Role.ROLE_ADMIN;

        User user = new User();
        user.setLogin("test");
        user.setPassword(encoder.encode("test"));
        user.setRole(role);

        repository.save(user);

        //when
        User userDB = service.findByLogin("test").get();

        //then
        assertThat(userDB, sameInstance(user));
    }

    @Test
    void DbShouldHaveAdminAndTeacherUsers() {
        //given
        service.initializeUsersTableWithBasicRecord();

        //when
        List<User> usersList = repository.findAll();

        //then
        assertAll(
                () -> assertThat(usersList.size(), equalTo(2)),
                () -> assertThat(usersList.get(0).getLogin(), equalTo("admin")),
                () -> assertThat(usersList.get(1).getLogin(), equalTo("teacher"))
        );
    }
}
