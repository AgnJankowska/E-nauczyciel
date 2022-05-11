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

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Wymagana nazwa przedmiotu")
    private String description;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "subject", orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private List<Section> sections;

    public Subject(String description) {
        this.description = description;
    }

    public void updateFrom(final Subject source) {
        description = source.description;
    }
}
