package pl.agnieszkajankowska.enauczyciel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.agnieszkajankowska.enauczyciel.model.SelectedValueContainer;
import pl.agnieszkajankowska.enauczyciel.model.Subject;
import pl.agnieszkajankowska.enauczyciel.service.SubjectService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("teacher/subjects")
@Controller
public class SubjectController {

    @Autowired
    private SubjectService service;

    @GetMapping
    String readAllSubject(Model model) {
        addModelAttribute(model);
        return "subjects";
    }

    @PostMapping
    String addSubject(
            @ModelAttribute("subject") @Valid Subject current,
            BindingResult bindingResult,
            Model model
    ) {
        //@Valid annotation
        if(bindingResult.hasErrors()) {
            return "subjects";
        }

        try {
            service.saveSubject(current);
            model.addAttribute("message", "Dodano przedmiot");
        } catch(Exception e) {
            model.addAttribute("messageErr", "Przedmiot nie został dodany - błąd bazy danych.");
        }

        addModelAttribute(model);
        model.addAttribute("subjectsList", getSubject());
        return "subjects";
    }

    @ModelAttribute("subjectsList")
    List<Subject> getSubject() {
        return service.readAll();
    }

    private void addModelAttribute(Model model) {
        model.addAttribute("subject", new Subject());
        model.addAttribute("container", new SelectedValueContainer());
    }
}
