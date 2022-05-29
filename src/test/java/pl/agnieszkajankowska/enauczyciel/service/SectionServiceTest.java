package pl.agnieszkajankowska.enauczyciel.service;

import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.agnieszkajankowska.enauczyciel.model.Section;
import pl.agnieszkajankowska.enauczyciel.model.Subject;

import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SectionServiceTest {

    @Autowired
    private SectionService sectionService;
    @Autowired
    private SubjectService subjectService;

    private Subject subject;

    @BeforeEach
    void shouldAddSubjectInDB() {
        subject = new Subject("test");
        subjectService.saveSubject(subject);
    }

    @Test
    void shouldSaveSectionInDB() {
        //given
        Section sectionToSave1 = new Section("Test pierwszy", subject);
        Section sectionToSave2 = new Section("Test drugi", subject);

        //when
        Section savedSection1 = sectionService.saveSection(sectionToSave1);
        Section savedSection2 = sectionService.saveSection(sectionToSave2);

        //then
        assertAll(
                () -> assertThat(savedSection1.getDescription(), equalTo("Test pierwszy")),
                () -> assertThat(savedSection2.getDescription(), equalTo("Test drugi")),
                () -> assertThat(savedSection1.getSubject(), sameInstance(subject)),
                () -> assertThat(savedSection2.getSubject(), sameInstance(subject))
        );
    }

    @Test
    void shouldEditSectionsDescriptionInDB() throws NotFoundException {
        //given
        Section sectionToSave = new Section("Przed edycją", subject);
        Section savedSection = sectionService.saveSection(sectionToSave);

        //when
        savedSection.setDescription("Po edycji");
        Section editedSection = sectionService.saveEditedSection(savedSection);

        //then
        assertThat(editedSection.getDescription(), equalTo("Po edycji"));
    }

    @Test
    void shouldEditSectionsSubjectInDB() throws NotFoundException {
        //given
        Section sectionToSave = new Section("Przed edycją", subject);
        Section savedSection = sectionService.saveSection(sectionToSave);

        Subject subjectNew = new Subject("Test");
        subjectService.saveSubject(subjectNew);

        //when
        savedSection.setSubject(subjectNew);
        Section editedSection = sectionService.saveEditedSection(savedSection);

        //then
        assertAll(
                () -> assertThat(editedSection.getSubject(), sameInstance(subjectNew)),
                () -> assertThat(editedSection.getSubject(), not(sameInstance(subject)))
        );
    }

    @Test
    void shouldThrowExceptionWhenIdOfEditedSectionsIsIncorrect() {
        //given
        Section sectionNotSavedInDB = new Section("Test", subject);

        //when
        //then
        assertThrows(NotFoundException.class,
                () -> sectionService.saveEditedSection(sectionNotSavedInDB));
    }

    @Test
    void shouldDeleteSectionFromDB() throws NotFoundException {
        //given
        Section section1 = new Section("Test do usunięcia", subject);
        Section section2 = new Section("Test do pozostawienia", subject);
        Section sectionToDelete = sectionService.saveSection(section1);
        Section sectionToMaintain = sectionService.saveSection(section2);

        //when
        sectionService.deleteSection(sectionToDelete);
        List<Section> sectionsList = sectionService.readBySubject(subject);

                //then
        assertAll(
                () -> assertThat(sectionsList.contains(sectionToDelete), is(false)),
                () -> assertThat(sectionsList.contains(sectionToMaintain), is(true))
        );
    }

    @Test
    void shouldThrowExceptionWhenIdOfDeletedSectionIsIncorrect() {
        //given
        Section sectionNotSavedInDB = new Section("Test", subject);

        //when
        //then
        assertThrows(NotFoundException.class,
                () -> sectionService.deleteSection(sectionNotSavedInDB));
    }

    @Test
    void shouldReturnAllSavedObject() {
        //given
        Section sectionToSave1 = new Section("Test", subject);
        Section sectionToSave2 = new Section("Test", subject);

        //when
        Section savedSection1 = sectionService.saveSection(sectionToSave1);
        Section savedSection2 = sectionService.saveSection(sectionToSave2);
        List<Section> sectionsList = sectionService.readBySubject(subject);

        //then
        assertAll(
                () -> assertThat(sectionsList.size(), equalTo(2)),
                () -> assertThat(sectionsList.get(0), equalTo(savedSection1)),
                () -> assertThat(sectionsList.get(1), equalTo(savedSection2))
        );
    }
}