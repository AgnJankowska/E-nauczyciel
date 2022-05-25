package pl.agnieszkajankowska.enauczyciel.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.agnieszkajankowska.enauczyciel.model.Section;
import pl.agnieszkajankowska.enauczyciel.model.SectionRepository;
import pl.agnieszkajankowska.enauczyciel.model.Subject;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    @Autowired
    private SectionRepository repository;

    public Section saveSection(final Section toSave) {
        toSave.setDescription(AxillaryService.formatTextBeforeSaveInDB(toSave.getDescription()));
        return repository.save(toSave);
    }

    public Section saveEditedSection(Section section)  throws NotFoundException {
        editedSection.updateFrom(section);
    }

    public void deleteSection(Section section) throws NotFoundException {
        delete(section.getId());
    }

    public List<Section> readBySubject(Subject subject) {
        return repository.findBySubject(subject);
    }


    //    PRIVATE METHODS
    private Optional<Section> findById(Integer i) {
        return repository.findById(i);
    }

    private void delete(Integer i) {
        repository.deleteById(i);
    }
}

