package pl.agnieszkajankowska.enauczyciel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Wymagana nazwa działu")
    private String description;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "section", orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private List<Assignment> assignments;

    public Section(String description, Subject subject) {
        this.description = description;
        this.subject = subject;
    }

    public void updateFrom(final Section source) {
        subject = Optional.ofNullable(source.getSubject()).orElse(this.subject);
        description = Optional.ofNullable(source.getDescription()).orElse(this.description);
    }
}
