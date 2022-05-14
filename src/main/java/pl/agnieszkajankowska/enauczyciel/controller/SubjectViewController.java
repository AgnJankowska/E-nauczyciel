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

@RequestMapping("teacher/subjectsView")
@Controller
public class SubjectViewController {

    @Autowired
    private SubjectService service;

    @GetMapping
    String readSubject(Model model) {
        addModelAttribute(model);
        return "subjectsView";
    }

    @PostMapping(params="action=edit")
    String editSubject(
            @ModelAttribute("subject") @Valid Subject subject,
            BindingResult bindingResult,
            Model model
    ) {
        //@Valid annotation
        if(bindingResult.hasErrors()) {
            return "subjectsView";
        }

        try {
            service.saveEditedSubject(subject);
            model.addAttribute("message", "Nazwa przedmiotu została zmieniona");
        } catch(Exception e) {
            e.printStackTrace();
            model.addAttribute("messageErr", "Nazwa nie została zmieniona - problem z bazą danych");
        }
        addModelAttribute(model);
        return "subjectsView";
    }

    @PostMapping(params="action=delete")
    String deleteSubject(
            @ModelAttribute("subject") Subject subject,
            BindingResult bindingResult,
            Model model
    ) {
        try {
            service.deleteSubject(subject);
            model.addAttribute("message", "Przedmiot został usunięty");
        } catch(Exception e) {
            model.addAttribute("messageErr", "Przedmiot nie został usunięty - problem z badą danych");
        }
        addModelAttribute(model);
        return "subjectsView";
    }

    @ModelAttribute("subjectsList")
    List<Subject> getSubject() {
        return service.readAll();
    }

    private void addModelAttribute(Model model) {
        model.addAttribute("subject", new Subject());
        model.addAttribute("container", new SelectedValueContainer());
        model.addAttribute("subjectsList", getSubject());
    }
}
