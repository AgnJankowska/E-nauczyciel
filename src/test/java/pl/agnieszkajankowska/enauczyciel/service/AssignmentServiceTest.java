package pl.agnieszkajankowska.enauczyciel.service;

import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.agnieszkajankowska.enauczyciel.model.Assignment;
import pl.agnieszkajankowska.enauczyciel.model.CorrectAnswer;
import pl.agnieszkajankowska.enauczyciel.model.Section;
import pl.agnieszkajankowska.enauczyciel.model.Subject;

import javax.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@Transactional
class AssignmentServiceTest {

    @Autowired
    AssignmentService assignmentService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    SectionService sectionService;

    Subject subject;
    Section section1;
    Section section2;
    Assignment assignment;
    MultipartFile multipartFile;

    static String pathToUploadDirectory = "src/test/resources/static/uploads/";

    @BeforeEach
    void saveSubjectAndSectionInDB() {
        subject = new Subject("Test");
        subjectService.saveSubject(subject);

        section1 = new Section("Test", subject);
        sectionService.saveSection(section1);

        section2 = new Section("Test", subject);
        sectionService.saveSection(section2);
    }

    @BeforeAll
    static void deleteAllUploadsFile() throws IOException {
        File directory = new File(pathToUploadDirectory);
        Files.walk(directory.toPath())
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void shouldSaveAssignmentInDBWithoutAnyFilePath() {
        //given
        Assignment assignmentToSave = createAssignment(section1);

        //when
        Assignment savedAssignment = assignmentService.saveAssignment(assignmentToSave);  //assignment without file
        List<Assignment> assignmentList = assignmentService.readBySection(section1);

        //then
        assertAll(
            () -> assertThat(assignmentList.contains(savedAssignment), is(true)),
            () -> assertThat(assignmentList.get(0).getPathToImage(), is(nullValue()))
        );
    }

    @Test
    void shouldSaveAssignmentAndFilesPathInDB() throws IOException {
        //given
        Assignment assignmentToSave = createAssignment(section1);

        //when
        Assignment savedAssignment = assignmentService.saveAssignmentAndFile(assignmentToSave, createMultiPartFile("test.txt"), pathToUploadDirectory);  //assignment with file
        List<Assignment> assignmentList = assignmentService.readBySection(section1);

        //then
        assertAll(
                () -> assertThat(assignmentList.contains(savedAssignment), is(true)),
                () -> assertThat(assignmentList.get(0).getPathToImage(), equalTo("/uploads/test.txt"))
        );
    }

    @Test
    void shouldSaveEditedAssignmentWhichHadNotPathToImage() throws NotFoundException, IOException {
        //given
        Assignment assignmentToSave = createAssignment(section1);
        assignmentService.saveAssignment(assignmentToSave);         //assignment without file

        //when
        assignmentToSave.setQuestion("Test question");              //changing some filed
        Assignment savedAssignment = assignmentService.saveEditedAssignmentWhichNotHadLoadedFile(assignmentToSave, createMultiPartFile("testedited.txt"), pathToUploadDirectory);  //adding file
        List<Assignment> assignmentList = assignmentService.readBySection(section1);

        //then
        assertAll(
                () -> assertThat(assignmentList.contains(savedAssignment), is(true)),
                () -> assertThat(assignmentList.get(0).getPathToImage(), equalTo("/uploads/testedited.txt")),
                () -> assertThat(assignmentList.get(0).getQuestion(), equalTo("Test question"))
        );
    }

    @Test
    void shouldThrowExceptionWhenIdOfEditedAssignmentIsIncorrectAndAssignmentHadNotPathToImage() {
        //given
        Assignment assignment = createAssignment(section1);    //assignment not saved in DB

        //when
        //then
        assertThrows(NotFoundException.class,
                () -> assignmentService.saveEditedAssignmentWhichNotHadLoadedFile(assignment, createMultiPartFile("test.txt"), pathToUploadDirectory));
    }

    @Test
    void shouldSaveEditedAssignmentWhichHadPathToImage() throws NotFoundException, IOException {
        //given
        Assignment assignmentToSave = createAssignment(section1);
        assignmentService.saveAssignmentAndFile(assignmentToSave, createMultiPartFile("test1.txt"), pathToUploadDirectory);   //assignment with file

        //when
        assignmentToSave.setPathToImage("test2");                   //changing file
        assignmentToSave.setQuestion("Test question");              //changing some other filed

        Assignment savedAssignment = assignmentService.saveEditedAssignmentWhichHadLoadedFile(assignmentToSave, createMultiPartFile("test2.txt"), pathToUploadDirectory);
        List<Assignment> assignmentList = assignmentService.readBySection(section1);

        //then
        assertAll(
                () -> assertThat(assignmentList.contains(savedAssignment), is(true)),
                () -> assertThat(assignmentList.get(0).getPathToImage(), equalTo("/uploads/test2.txt")),
                () -> assertThat(assignmentList.get(0).getQuestion(), equalTo("Test question"))
        );
    }

    @Test
    void shouldThrowExceptionWhenIdOfEditedAssignmentWithLoadedFileIsIncorrect() {
        //given
        Assignment assignment = createAssignment(section1);    //assignment not saved in DB

        //when
        //then
        assertThrows(NotFoundException.class,
                () -> assignmentService.saveEditedAssignmentWhichHadLoadedFile(assignment, createMultiPartFile("test.txt"), pathToUploadDirectory));
    }

    @Test
    void shouldDeleteAssignmentFromDB() throws NotFoundException, IOException {
        //given
        Assignment assignment1 = createAssignment(section1);
        Assignment assignment2 = createAssignment(section1);

        Assignment assignmentToDelete = assignmentService.saveAssignment(assignment1);
        Assignment assignmentToMaintain = assignmentService.saveAssignment(assignment2);

        //when
        assignmentService.deleteAssignmentById(assignmentToDelete);
        List<Assignment> listOfAllAssignment = assignmentService.readBySection(section1);

        //then
        assertAll(
                () -> assertThat(listOfAllAssignment.contains(assignmentToDelete), is(false)),
                () -> assertThat(listOfAllAssignment.contains(assignmentToMaintain), is(true))
        );
    }

    @Test
    void shouldThrowExceptionWhenIdOfDeletedAssignmentIsIncorrect() {
        //given
        Assignment assignment = createAssignment(section1);    //assignment not saved in DB

        //when
        //then
        assertThrows(NotFoundException.class,
                () -> assignmentService.deleteAssignmentById(assignment));
    }

    @Test
    void shouldReturnAllAssignmentForAllSectionDescribedToSubject() {
        //given
        Assignment assignment1 = createAssignment(section1);
        Assignment assignment2 = createAssignment(section2);

        assignmentService.saveAssignment(assignment1);
        assignmentService.saveAssignment(assignment2);

        //when
        List<Assignment> listOfAllAssignment = assignmentService.readBySubject(subject);

        //then
        assertAll(
                () -> assertThat(listOfAllAssignment.size(), equalTo(2)),
                () -> assertThat(listOfAllAssignment.get(0).getSection(), sameInstance(section1)),
                () -> assertThat(listOfAllAssignment.get(1).getSection(), sameInstance(section2))
        );
    }

    @Test
    void shouldReturnEmptyListWhenThereIsNoAssignmentInDB() {
        //given
        //when
        List<Assignment> listOfAllAssignment = assignmentService.readBySubject(subject);

        //then
        assertThat(listOfAllAssignment, is(empty()));
    }

    @Test
    void shouldReturnFirstAssignmentFormListBasedOnSection() {
        //given
        Assignment assignment1 = createAssignment(section1);
        Assignment assignment2 = createAssignment(section1);

        assignmentService.saveAssignment(assignment1);
        assignmentService.saveAssignment(assignment2);

        List<Assignment> assignmentsList = assignmentService.readBySection(section1);

        //when
        Assignment assignment = assignmentService.getFirstAssignmentOrNullBySection(section1);

        //then
        assertThat(assignment, equalTo(assignmentsList.get(0)));
    }

    @Test
    void shouldReturnNullFormListBasedOnSection() {
        //given
        Assignment assignment = assignmentService.getFirstAssignmentOrNullBySection(section1);

        //when
        //then
        assertThat(assignment, is(nullValue()));
    }

    @Test
    void shouldReturnFirstAssignmentFormListBasedOnSubject() {
        //given
        Assignment assignment1 = createAssignment(section1);
        Assignment assignment2 = createAssignment(section1);

        assignmentService.saveAssignment(assignment1);
        assignmentService.saveAssignment(assignment2);

        List<Assignment> assignmentsList = assignmentService.readBySubject(subject);

        //when
        Assignment assignment = assignmentService.getFirstAssignmentOrNullBySubject(subject);

        //then
        assertThat(assignment, equalTo(assignmentsList.get(0)));
    }

    @Test
    void shouldReturnNullAssignmentBasedOnSubject() {
        //given
        Assignment assignment = assignmentService.getFirstAssignmentOrNullBySubject(subject);

        //when
        //then
        assertThat(assignment, is(nullValue()));
    }

    @Test
    void shouldReturnNextAssignmentFormList() {
        //given
        Assignment assignment1 = createAssignment(section1);
        Assignment assignment2 = createAssignment(section1);
        Assignment assignment3 = createAssignment(section1);

        assignmentService.saveAssignment(assignment1);
        assignmentService.saveAssignment(assignment2);
        assignmentService.saveAssignment(assignment3);
        List<Assignment> assignmentsList = assignmentService.readBySubject(subject);

        //when
        Assignment previousAssignment1 = assignmentsList.get(0);
        Assignment previousAssignment2 = assignmentsList.get(1);
        Assignment nextAssignment1 = assignmentService.getNextAssignment(previousAssignment1, assignmentsList);
        Assignment nextAssignment2 = assignmentService.getNextAssignment(previousAssignment2, assignmentsList);

        //then
        assertAll(
                () -> assertThat(nextAssignment1, equalTo(assignmentsList.get(1))),
                () -> assertThat(nextAssignment2, equalTo(assignmentsList.get(2)))
        );
    }

    @Test
    void shouldReturnFirstAssignmentFromListWhenWholeSeriesEnds() {
        //given
        Assignment assignment1 = createAssignment(section1);
        Assignment assignment2 = createAssignment(section1);
        Assignment assignment3 = createAssignment(section1);

        assignmentService.saveAssignment(assignment1);
        assignmentService.saveAssignment(assignment2);
        assignmentService.saveAssignment(assignment3);

        List<Assignment> assignmentsList = assignmentService.readBySubject(subject);

        //when
        Assignment previousAssignment = assignmentsList.get(2);
        Assignment nextAssignment = assignmentService.getNextAssignment(previousAssignment, assignmentsList);

        //then
        assertThat(nextAssignment, equalTo(assignmentsList.get(0)));
    }

    @Test
    void shouldReturnAssignmentListIfNumberOfAssignmentIsGraterOrEqualToMinimum() {
        //given
        int numberOfTestingAssignment = assignmentService.getNumberOfTestingAssignment();
        for(int i=0; i<numberOfTestingAssignment; i++) {    //add minimum required number of assignment to db
            Assignment assignment = createAssignment(section1);
            assignmentService.saveAssignment(assignment);
        }

        //when
        List<Assignment> assignmentListForTesting = assignmentService.getAssignmentToCreateTestOrNullIfToSmallAmount(subject);

        //then
        assertAll(
                () -> assertThat(assignmentListForTesting, not(empty())),
                () -> assertThat(assignmentListForTesting.size(), equalTo(numberOfTestingAssignment))
        );
    }

    @Test
    void shouldReturnNullIfNumberOfAssignmentIsSmallerThanMinimum() {
        //given
        int numberOfTestingAssignment = assignmentService.getNumberOfTestingAssignment();
        for(int i=0; i<numberOfTestingAssignment-1; i++) {        //add one less than required assignment to db
            Assignment assignment = createAssignment(section1);
            assignmentService.saveAssignment(assignment);
        }

        //when
        List<Assignment> assignmentListForTesting = assignmentService.getAssignmentToCreateTestOrNullIfToSmallAmount(subject);

        //then
        assertThat(assignmentListForTesting, is(nullValue()));
    }

    Assignment createAssignment(Section section) {
        assignment = new Assignment();
        assignment.setAnswerA("Odp a");
        assignment.setAnswerB("Odp a");
        assignment.setAnswerC("Odp a");
        assignment.setAnswerD("Odp a");
        assignment.setCorrectAnswer(CorrectAnswer.a);
        assignment.setQuestion("Treść");
        assignment.setSection(section);

        return assignment;
    }

    MultipartFile createMultiPartFile(String name) {
        multipartFile = new MockMultipartFile(
                "file",
                name,
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        return multipartFile;
    }
}