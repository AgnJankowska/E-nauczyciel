package pl.agnieszkajankowska.enauczyciel.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    List<Assignment> findAll();

    List<Assignment> findBySection(Section section);

    Assignment save(Assignment toCreate);

    Optional<Assignment> findById(Integer i);

    void deleteById(Integer i);

    boolean existsById(Integer id);

}
