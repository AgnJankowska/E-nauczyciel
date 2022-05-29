package pl.agnieszkajankowska.enauczyciel.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.agnieszkajankowska.enauczyciel.service.UserService;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    @Test
    void shouldReturnNewInstanceOfMyUserDetails() throws UsernameNotFoundException{
        //given
        userService.initializeUsersTableWithBasicRecord();
        String usernameInDB = "admin";

        //when
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameInDB);

        //then
        assertThat(userDetails, is(notNullValue()));
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsIncorrect() {
        //given
        userService.initializeUsersTableWithBasicRecord();
        String usernameNotInDB = "test";

        //when
        //then
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(usernameNotInDB));
    }
}