package pl.agnieszkajankowska.enauczyciel.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository  extends JpaRepository<Section, Integer> {

    List<Section> findBySubject(Subject subject);

    Section save(Section toCreate);

    Optional<Section> findById(Integer i);

    void deleteById(Integer i);

    boolean existsById(Integer id);

}
