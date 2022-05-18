package pl.agnieszkajankowska.enauczyciel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.agnieszkajankowska.enauczyciel.model.Assignment;
import pl.agnieszkajankowska.enauczyciel.model.Section;
import pl.agnieszkajankowska.enauczyciel.model.SelectedValueContainer;
import pl.agnieszkajankowska.enauczyciel.model.Subject;
import pl.agnieszkajankowska.enauczyciel.service.AssignmentService;

import java.util.List;

@Controller
@RequestMapping("student/learn/subjects")
public class AssignmentLearningController {

    @Autowired
    private AssignmentService service;

    @GetMapping("/sections/assignments")
    public String readFirstAssignmentBySection(
            @ModelAttribute("container") SelectedValueContainer container,
            BindingResult bindingResult,
            Model model
    ) {
        model.addAttribute("assignment", service.getFirstAssignmentOrNullBySection(container.getSection()));
        addModelAttribute(model, container.getSection());
        return "assignmentsLearning";
    }

    @GetMapping("/allAssignments")
    public String readFirstAssignmentBySubject(
            @ModelAttribute("container") SelectedValueContainer container,
            BindingResult bindingResult,
            Model model
    ) {
        model.addAttribute("assignment", service.getFirstAssignmentOrNullBySubject(container.getSubject()));
        addModelAttributeAllAssignment(model, container.getSubject());
        return "assignmentsLearning";
    }

    private void addModelAttribute(Model model, Section section) {
        model.addAttribute("section", section);
        model.addAttribute("subject", section.getSubject());
        model.addAttribute("assignmentsList", getAssignmentBySection(section));
    }

    private void addModelAttributeAllAssignment(Model model, Subject subject) {
        model.addAttribute("subject", subject);
        model.addAttribute("assignmentsList", getAssignmentBySubject(subject));
    }
}
