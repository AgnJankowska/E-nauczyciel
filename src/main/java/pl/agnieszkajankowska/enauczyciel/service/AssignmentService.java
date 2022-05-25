package pl.agnieszkajankowska.enauczyciel.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.agnieszkajankowska.enauczyciel.model.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final int numberOfTestingAssignment = 5;

    @Autowired
    private AssignmentRepository repository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ImageService imageService;

    public Assignment saveAssignment(final Assignment toSave) {
           return repository.save(toSave);
    }

    public Assignment saveAssignmentAndFile(Assignment selectedAssignment, MultipartFile file, String pathToUploadDirectory) throws IOException {
        if(!file.isEmpty()) {
            selectedAssignment.setPathToImage(getAttachedImagePath(file, pathToUploadDirectory)); //dodanie obrazka
        }
        return saveAssignment(selectedAssignment);
    }

    public Assignment saveEditedAssignmentWhichNotHadLoadedFile(Assignment assignment, MultipartFile file, String pathToUploadDirectory) throws NotFoundException, IOException  {
        return saveAssignmentAndFile(assignment, file, pathToUploadDirectory);
    }

    public Assignment saveEditedAssignmentWhichHadLoadedFile(Assignment assignment, MultipartFile file, String pathToUploadDirectory) throws NotFoundException, IOException  {
        return saveAssignmentAfterEditionWithLoadedFile(assignment, file, pathToUploadDirectory);
    }

    public void deleteAssignmentById(Assignment assignment) throws NotFoundException {
        deleteById(assignment.getId());
    }

    public List<Assignment> readBySection(Section section) {
        return repository.findBySection(section);
    }

    public List<Assignment> readBySubject(Subject subject) {
        return repository.findBySection(section);
    }

    public Assignment getFirstAssignmentOrNullBySection(Section section) {
        List<Assignment> assignmentsList = readBySection(section);
        return assignmentsList.get(0);
    }

    public Assignment getFirstAssignmentOrNullBySubject(Subject subject) {
        List<Assignment> assignmentsList = readBySubject(subject);
        return assignmentsList.get(0);
    }

    public Assignment getNextAssignment(Assignment previousAssignment, List<Assignment> assignmentsList) {
        return assignmentsList.get(position+1);
    }

    public List<Assignment> getAssignmentToCreateTestOrNullIfToSmallAmount(Subject subject) {

        List<Assignment> assignmentsList = readTestAssignmentBySubject(subject);

        if(assignmentsList.size() >= numberOfTestingAssignment) {
            return assignmentsList;
        } else {
            return null;
        }
    }

    public int getNumberOfTestingAssignment() {
        return numberOfTestingAssignment;
    }


    //    PRIVATE METHODS
    private Assignment saveAssignmentAfterEditionWithLoadedFile(Assignment selectedAssignment, MultipartFile file, String pathToUploadDirectory) throws IOException {
        selectedAssignment.setPathToImage(getAttachedImagePath(file, pathToUploadDirectory));
        return saveAssignment(selectedAssignment);        
    }

    private void deleteById(Integer i) {
        repository.deleteById(i);
    }

    private Optional<Assignment> findById(Integer i) {
        return repository.findById(i);
    }
}
