package pl.agnieszkajankowska.enauczyciel.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.agnieszkajankowska.enauczyciel.model.Subject;
import pl.agnieszkajankowska.enauczyciel.model.SubjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository repository;

    public Subject saveSubject(Subject toSave) {
        toSave.setDescription(AxillaryService.formatTextBeforeSaveInDB(toSave.getDescription()));
        return repository.save(toSave);
    }

    public Subject saveEditedSubject(Subject subject) throws NotFoundException {

        Optional<Subject> editedSubjectOptional = findById(subject.getId());

        if(editedSubjectOptional.isPresent()) {
            Subject editedSubject = editedSubjectOptional.get();
            editedSubject.updateFrom(subject);
            return saveSubject(editedSubject);
        } else {
            throw new NotFoundException("niewłaściwe ID");
        }
    }

    public void deleteSubject(Subject subject) throws NotFoundException {
        if(findById(subject.getId()).isPresent()) {
            delete(subject.getId());
        } else {
            throw new NotFoundException("niewłaściwe ID");
        }
    }

    public List<Subject> readAll() {
        return repository.findAll();
    }

    //  PRIVATE METHODS
    private Optional<Subject> findById(Integer i) {
        return repository.findById(i);
    }

    private void delete(Integer i) {
        repository.deleteById(i);
    }
}
