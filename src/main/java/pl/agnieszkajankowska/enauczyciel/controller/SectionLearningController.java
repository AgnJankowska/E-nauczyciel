package pl.agnieszkajankowska.enauczyciel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.agnieszkajankowska.enauczyciel.model.Section;
import pl.agnieszkajankowska.enauczyciel.model.SelectedValueContainer;
import pl.agnieszkajankowska.enauczyciel.model.Subject;
import pl.agnieszkajankowska.enauczyciel.service.SectionService;

import java.util.List;

@Controller
@RequestMapping("student/learn/subjects/sections")
public class SectionLearningController {

    @Autowired
    private SectionService service;

    @GetMapping
    public String readAllSection(
            @ModelAttribute("container") SelectedValueContainer container,
            BindingResult bindingResult,
            Model model
    ) {
        addModelAttribute(model, container.getSubject());
        return "sectionsLearning";
    }

    private void addModelAttribute(Model model, Subject subject) {
        model.addAttribute("subject", subject);
        model.addAttribute("section", new Section());
    }
}
