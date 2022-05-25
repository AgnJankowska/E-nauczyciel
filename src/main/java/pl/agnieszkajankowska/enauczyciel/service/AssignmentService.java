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
        toSave.setQuestion(AxillaryService.formatTextBeforeSaveInDB(toSave.getQuestion()));
        toSave.setAnswerA(AxillaryService.formatTextBeforeSaveInDB(toSave.getAnswerA()));
        toSave.setAnswerB(AxillaryService.formatTextBeforeSaveInDB(toSave.getAnswerB()));
        toSave.setAnswerC(AxillaryService.formatTextBeforeSaveInDB(toSave.getAnswerC()));
        toSave.setAnswerD(AxillaryService.formatTextBeforeSaveInDB(toSave.getAnswerD()));

        return repository.save(toSave);
    }

    public Assignment saveAssignmentAndFile(Assignment selectedAssignment, MultipartFile file, String pathToUploadDirectory) throws IOException {
        if(!file.isEmpty()) {
            selectedAssignment.setPathToImage(getAttachedImagePath(file, pathToUploadDirectory)); //dodanie obrazka
        }
        return saveAssignment(selectedAssignment);
    }

    public Assignment saveEditedAssignmentWhichNotHadLoadedFile(Assignment assignment, MultipartFile file, String pathToUploadDirectory) throws NotFoundException, IOException  {
        if (findById(assignment.getId()).isPresent()) {
            return saveAssignmentAndFile(assignment, file, pathToUploadDirectory);
        } else {
            throw new NotFoundException("niewłaściwe ID");
        }
    }

    public Assignment saveEditedAssignmentWhichHadLoadedFile(Assignment assignment, MultipartFile file, String pathToUploadDirectory) throws NotFoundException, IOException  {
        if (findById(assignment.getId()).isPresent()) {
            return saveAssignmentAfterEditionWithLoadedFile(assignment, file, pathToUploadDirectory);
        } else {
            throw new NotFoundException("niewłaściwe ID");
        }
    }

    public void deleteAssignmentById(Assignment assignment) throws NotFoundException {
        Optional<Assignment> assignmentDB = findById(assignment.getId());

        if(assignmentDB.isPresent()) {
            deleteById(assignment.getId());
        } else {
            throw new NotFoundException("niewłaściwe ID");
        }
    }

    public List<Assignment> readBySection(Section section) {
        return repository.findBySection(section);
    }

    public List<Assignment> readBySubject(Subject subject) {

        List<Section> sectionsList = sectionRepository.findBySubject(subject);
        List<Assignment> assignmentsList = new ArrayList<>();

        if(!sectionsList.isEmpty()) {
            for(Section section : sectionsList) {
                assignmentsList.addAll(repository.findBySection(section));
            }
        }
        return assignmentsList;
    }

    public Assignment getFirstAssignmentOrNullBySection(Section section) {
        List<Assignment> assignmentsList = readBySection(section);

        if(assignmentsList!=null && !assignmentsList.isEmpty()) {
            return assignmentsList.get(0);
        } else {
            return null;
        }
    }

    public Assignment getFirstAssignmentOrNullBySubject(Subject subject) {
        List<Assignment> assignmentsList = readBySubject(subject);

        if(assignmentsList!=null && !assignmentsList.isEmpty()) {
            return assignmentsList.get(0);
        } else {
            return null;
        }
    }

    public Assignment getNextAssignment(Assignment previousAssignment, List<Assignment> assignmentsList) {
        Assignment assignmentsListElem = assignmentsList
                .stream()
                .filter(e -> e.getId() == previousAssignment.getId())
                .collect(Collectors.toList())
                .get(0);
        int position = assignmentsList.indexOf(assignmentsListElem);

        if(position < assignmentsList.size()-1) {
            return assignmentsList.get(position+1);
        }
        return assignmentsList.get(0);   //ponowne wyświetlenie 1 zadania
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
        if(file.isEmpty()) {
            selectedAssignment.setPathToImage(null);                    //usunięcie obrazka
        } else {
            selectedAssignment.setPathToImage(getAttachedImagePath(file, pathToUploadDirectory));
        }
        return saveAssignment(selectedAssignment);
    }

    private void deleteById(Integer i) {
        repository.deleteById(i);
    }

    private Optional<Assignment> findById(Integer i) {
        return repository.findById(i);
    }

    private String getAttachedImagePath(MultipartFile file, String pathToUploadDirectory) throws IOException {
        return imageService.saveAttachedImage(file, pathToUploadDirectory);
    }

    private List<Assignment> readTestAssignmentBySubject(Subject currentSubject) {
        List<Assignment> assignmentsList = readBySubject(currentSubject);
        Collections.shuffle(assignmentsList);

        return assignmentsList.stream().limit(numberOfTestingAssignment).collect(Collectors.toList());
    }

}
