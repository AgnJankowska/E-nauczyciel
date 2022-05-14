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
@RequestMapping("teacher/subjectsView/sectionsView")
public class SectionViewController {

    @Autowired
    private SectionService service;

    @GetMapping
    public String readSection(
            @ModelAttribute("container") SelectedValueContainer container,
            BindingResult bindingResult,
            Model model
    ) {
        addModelAttribute(model, container.getSubject());
        return "sectionsView";
    }

    @ModelAttribute("sectionsList")
    List<Section> getSection(Subject currentSubject) {
        return service.readBySubject(currentSubject);
    }

    private void addModelAttribute(Model model, Subject subject) {
        model.addAttribute("subject", subject);
        model.addAttribute("section", new Section());
        model.addAttribute("container", new SelectedValueContainer());
        model.addAttribute("sectionsList", getSection(subject));
    }
}
