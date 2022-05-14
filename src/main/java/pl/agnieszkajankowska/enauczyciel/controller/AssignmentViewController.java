package pl.agnieszkajankowska.enauczyciel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.agnieszkajankowska.enauczyciel.model.Assignment;
import pl.agnieszkajankowska.enauczyciel.model.SelectedValueContainer;
import pl.agnieszkajankowska.enauczyciel.model.Section;
import pl.agnieszkajankowska.enauczyciel.service.AssignmentService;
import pl.agnieszkajankowska.enauczyciel.service.ImageService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("teacher/subjectsView/sectionsView/assignmentsView")
public class AssignmentViewController {

    @Autowired
    private AssignmentService service;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public String readAssignment(
            @ModelAttribute("container") SelectedValueContainer container,
            BindingResult bindingResult,
            Model model
    ) {
        addModelAttribute(model, container.getSection());
        return "assignmentsView";
    }

    @GetMapping("/modify")
    public String modifyAssignment(
            @ModelAttribute("assignment") Assignment selectedAssignment,
            BindingResult bindingResult,
            Model model
    ) {
        addModelAttributeModify(model, selectedAssignment);
        return "assignmentsModify";
    }

    @ModelAttribute("assignmentsList")
    List<Assignment> getAssignments(Section currentSection) {
        return service.readBySection(currentSection);
    }

    private void addModelAttributeModify(Model model, Assignment assignment) {
        model.addAttribute("section", assignment.getSection());
        model.addAttribute("assignment", assignment);
    }
}
