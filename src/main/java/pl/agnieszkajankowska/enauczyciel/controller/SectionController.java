package pl.agnieszkajankowska.enauczyciel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.agnieszkajankowska.enauczyciel.model.SelectedValueContainer;
import pl.agnieszkajankowska.enauczyciel.model.Section;
import pl.agnieszkajankowska.enauczyciel.model.Subject;
import pl.agnieszkajankowska.enauczyciel.service.SectionService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("teacher/subjects/sections")
public class SectionController {

    @Autowired
    private SectionService service;

    @GetMapping
    public String readAllSection(
            @ModelAttribute("container") SelectedValueContainer container,
            BindingResult bindingResult,
            Model model
    ) {
        addModelAttribute(model, container.getSubject());
        return "sections";
    }

    @PostMapping
    String addSection(
            @ModelAttribute("section") @Valid Section section,
            @RequestParam int subject,
            BindingResult bindingResult,
            Model model
    ) {
        //@Valid annotation
        if(bindingResult.hasErrors()) {
            return "sections";
        }

        Subject selectedSubject = section.getSubject();

        try {
            service.saveSection(section);
            model.addAttribute("message", "Dodano dział");
        } catch (Exception e) {
            model.addAttribute("messageErr", "Dział nie został dodany - błąd bazy danych.");
        }

        addModelAttribute(model, selectedSubject);
        return "sections";
    }

    @ModelAttribute("sectionsList")
    List<Section> getSection(Subject currentSubject) {
        return service.readBySubject(currentSubject);
    }

    private void addModelAttribute(Model model, Subject selectedSubject) {
        model.addAttribute("subject", selectedSubject);
        model.addAttribute("section", new Section());
        model.addAttribute("container", new SelectedValueContainer());
        model.addAttribute("sectionsList", getSection(selectedSubject));
    }
}
