package pl.agnieszkajankowska.enauczyciel.model;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.agnieszkajankowska.enauczyciel.security.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);

    List<User> findAll();

    User save(User toCreate);
}
