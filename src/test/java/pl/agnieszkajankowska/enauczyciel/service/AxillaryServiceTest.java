package pl.agnieszkajankowska.enauczyciel.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AxillaryServiceTest {

    @Test
    void shouldReturnFormattedText() {
        //given
        String textWithOnlySmallLetter = "test test";
        String textWithOnlyCapitalLetter = "TEST TEST";
        String textWithMixedLetter = "TeSt tEsT";

        //when
        //then
        assertAll(
                () -> assertThat(AxillaryService.formatTextBeforeSaveInDB(textWithOnlySmallLetter), equalTo("Test test")),
                () -> assertThat(AxillaryService.formatTextBeforeSaveInDB(textWithOnlyCapitalLetter), equalTo("Test test")),
                () -> assertThat(AxillaryService.formatTextBeforeSaveInDB(textWithMixedLetter), equalTo("Test test"))
        );
    }
}