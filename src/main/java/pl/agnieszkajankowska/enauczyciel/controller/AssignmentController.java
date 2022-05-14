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
import pl.agnieszkajankowska.enauczyciel.model.Subject;
import pl.agnieszkajankowska.enauczyciel.service.AssignmentService;
import pl.agnieszkajankowska.enauczyciel.service.ImageService;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("teacher/subjects/sections/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService service;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public String readAllAssignments(
            @ModelAttribute("container") SelectedValueContainer container,
            BindingResult bindingResult,
            Model model
    ) {
        Section selectedSection = container.getSection();
        Subject selectedSubject = selectedSection.getSubject();

        addModelAttribute(model, selectedSection, selectedSubject);
        return "assignments";
    }

    @PostMapping
    String addAssignment(
            @ModelAttribute("assignment") @Valid Assignment assignment,
            @RequestPart(name = "formFile") MultipartFile file,
            @RequestParam int section,
            BindingResult bindingResult,
            Model model
    ) {
        //@Valid annotation
        if(bindingResult.hasErrors()) {
            return "assignments";
        }

        Section selectedSection = assignment.getSection();
        Subject selectedSubject = selectedSection.getSubject();

        try {
            service.saveAssignmentAndFile(assignment, file, imageService.getPathToAttachedImage());
            model.addAttribute("message", "Poprawnie dodano zadanie!");
        } catch (IOException e) {
            model.addAttribute("messageErr", "Zadanie nie zostało dodane - błąd bazy danych");
        }

        addModelAttribute(model, selectedSection, selectedSubject);
        return "assignments";
    }

    private void addModelAttribute(Model model, Section selectedSection, Subject selectedSubject) {
        model.addAttribute("assignment", new Assignment());
        model.addAttribute("section", selectedSection);
        model.addAttribute("subject", selectedSubject);
    }
}
