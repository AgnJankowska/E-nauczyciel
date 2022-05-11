package pl.agnieszkajankowska.enauczyciel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Wymagana treść pytania")
    private String question;
    @NotBlank(message = "Wymagana treść odpA")
    private String answerA;
    @NotBlank(message = "Wymagana treść odpB")
    private String answerB;
    @NotBlank(message = "Wymagana treść odpC")
    private String answerC;
    @NotBlank(message = "Wymagana treść odpD")
    private String answerD;
    private String pathToImage;

    @Enumerated(EnumType.STRING)
    private CorrectAnswer correctAnswer;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    public Assignment(String question, String answerA, String answerB, String answerC, String answerD, String pathToImage, CorrectAnswer correctAnswer, Section section) {
        this.question = question;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.pathToImage = pathToImage;
        this.correctAnswer = correctAnswer;
        this.section = section;
    }
}
