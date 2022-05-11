package pl.agnieszkajankowska.enauczyciel.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    List<Subject> findAll();

    Subject save(Subject toCreate);

    Optional<Subject> findById(Integer i);

    void deleteById(Integer i);

    boolean existsById(Integer id);
}
