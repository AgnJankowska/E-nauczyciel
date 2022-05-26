package pl.agnieszkajankowska.enauczyciel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.agnieszkajankowska.enauczyciel.model.SelectedValueContainer;
import pl.agnieszkajankowska.enauczyciel.model.Subject;
import pl.agnieszkajankowska.enauczyciel.service.AssignmentService;

@RequestMapping("student/test/subjects")
@Controller
public class AssignmentTestingController {

    @Autowired
    private AssignmentService service;

    @GetMapping
    String readTest(
            @ModelAttribute("container") SelectedValueContainer container,
            BindingResult bindingResult,
            Model model
    ) {
        Subject subject = container.getSubject();

        model.addAttribute("subject", subject);
        model.addAttribute("assignmentsList", service.getAssignmentToCreateTestOrNullIfToSmallAmount(subject));
        model.addAttribute("numberOfTestingAssignment", service.getNumberOfTestingAssignment());
        return "assignmentsTesting";
    }
}
