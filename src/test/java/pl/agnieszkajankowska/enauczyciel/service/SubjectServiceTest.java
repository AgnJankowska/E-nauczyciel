package pl.agnieszkajankowska.enauczyciel.service;

import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.agnieszkajankowska.enauczyciel.model.Subject;

import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class SubjectServiceTest {

    @Autowired
    private SubjectService subjectService;

    @Test
    void shouldSaveSubjectInDBWith() {
        //given
        Subject subjectToSave1 = new Subject("Test pierwszy");
        Subject subjectToSave2 = new Subject("Test drugi");

        //when
        Subject savedSubject1 = subjectService.saveSubject(subjectToSave1);
        Subject savedSubject2 = subjectService.saveSubject(subjectToSave2);

        //then
        assertAll(
                () -> assertThat(savedSubject1.getDescription(), equalTo("Test pierwszy")),
                () -> assertThat(savedSubject2.getDescription(), equalTo("Test drugi"))
        );
    }

    @Test
    void shouldEditSubjectInDBWithProperFormatText() throws NotFoundException {
        //given
        Subject subjectToSave = new Subject("Test przed edycją");
        Subject savedSubject = subjectService.saveSubject(subjectToSave);

        //when
        savedSubject.setDescription("Test po edycji");
        Subject editedSubject = subjectService.saveEditedSubject(savedSubject);

        //then
        assertThat(editedSubject.getDescription(), equalTo("Test po edycji"));
    }

    @Test
    void shouldThrowExceptionWhenIdOfEditedSubjectIsIncorrect() {
        //given
        Subject subjectNotSavedInDB = new Subject("Test");

        //when
        //then
        assertThrows(NotFoundException.class,
                () -> subjectService.saveEditedSubject(subjectNotSavedInDB));
    }

    @Test
    void shouldDeleteSubjectFromDB() throws NotFoundException {
        //given
        Subject subject1 = new Subject("Test");
        Subject subject2 = new Subject("Test");
        Subject subjectToDelete = subjectService.saveSubject(subject1);
        Subject subjectToMaintain = subjectService.saveSubject(subject2);

        //when
        subjectService.deleteSubject(subjectToDelete);
        List<Subject> subjectsList = subjectService.readAll();

        //then
        assertAll(
                () -> assertThat(subjectsList.contains(subjectToDelete), is(false)),
                () -> assertThat(subjectsList.contains(subjectToMaintain), is(true))
        );
    }

    @Test
    void shouldThrowExceptionWhenIdOfDeletedSubjectIsIncorrect() {
        //given
        Subject subjectNotSavedInDB = new Subject("Test");

        //when
        //then
        assertThrows(NotFoundException.class,
                () -> subjectService.deleteSubject(subjectNotSavedInDB));
    }

    @Test
    void shouldReturnAllSavedObject() {
        //given
        Subject subjectToSave1 = new Subject("Test");
        Subject subjectToSave2 = new Subject("Test");

        //when
        Subject savedSubject1 = subjectService.saveSubject(subjectToSave1);
        Subject savedSubject2 = subjectService.saveSubject(subjectToSave2);
        List<Subject> subjectsList = subjectService.readAll();

        //then
        assertAll(
                () -> assertThat(subjectsList.size(), equalTo(2)),
                () -> assertThat(subjectsList.get(0), equalTo(savedSubject1)),
                () -> assertThat(subjectsList.get(1), equalTo(savedSubject2))
        );
    }
}