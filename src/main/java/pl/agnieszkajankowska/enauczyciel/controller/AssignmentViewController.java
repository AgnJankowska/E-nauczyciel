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

    @PostMapping(value="/modify", params="action=withoutLoadedImage")
    String editAssignmentWhichNotHadLoadedFile(
            @ModelAttribute("assignment") @Valid Assignment selectedAssignment,
            @RequestPart(name = "formFile") MultipartFile file,
            BindingResult bindingResult,
            Model model
    ) {
        //@Valid annotation
        if(bindingResult.hasErrors()) {
            return "assignmentsModify";
        }

        try {
            service.saveEditedAssignmentWhichNotHadLoadedFile(selectedAssignment, file, imageService.getPathToAttachedImage());
            model.addAttribute("message", "Zadanie zostało zmienione!");
        } catch (Exception e) {
            model.addAttribute("messageErr", "Zadanie nie zostało dodane - problem z bazą danych");
        }

        addModelAttributeModify(model, selectedAssignment);
        return "assignmentsModify";
    }

    @PostMapping(value="/modify", params="action=withLoadedImage")
    String editAssignmentWhichHadLoadedFile(
            @ModelAttribute("assignment") @Valid Assignment selectedAssignment,
            @RequestPart(name = "formFile") MultipartFile file,
            BindingResult bindingResult,
            Model model
    ) {
        //@Valid annotation
        if(bindingResult.hasErrors()) {
            return "assignmentsModify";
        }

        try {
            service.saveEditedAssignmentWhichHadLoadedFile(selectedAssignment, file, imageService.getPathToAttachedImage());
            model.addAttribute("message", "Zadanie zostało zmienione!");
        } catch (Exception e) {
            model.addAttribute("messageErr", "Zadanie nie zostało dodane - problem z bazą danych");
        }

        addModelAttributeModify(model, selectedAssignment);
        return "assignmentsModify";
    }

    @PostMapping(params="action=delete")
    String deleteAssignment(
            @ModelAttribute("assignment") Assignment selectedAssignment,
            BindingResult bindingResult,
            Model model
    ) {
        try {
            service.deleteAssignmentById(selectedAssignment);
            model.addAttribute("message", "Zadanie zostało usunięte");
        } catch(Exception e) {
            model.addAttribute("messageErr", "Zadanie nie zostało usunięte - problem z badą danych");
        }

        addModelAttribute(model, selectedAssignment.getSection());
        return "assignmentsView";
    }

    @ModelAttribute("assignmentsList")
    List<Assignment> getAssignments(Section currentSection) {
        return service.readBySection(currentSection);
    }

    private void addModelAttribute(Model model, Section section) {
        model.addAttribute("assignment", new Assignment());
        model.addAttribute("section", section);
        model.addAttribute("subject", section.getSubject());
        model.addAttribute("assignmentsList", getAssignments(section));
    }

    private void addModelAttributeModify(Model model, Assignment assignment) {
        model.addAttribute("section", assignment.getSection());
        model.addAttribute("assignment", assignment);
    }
}
