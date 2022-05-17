package pl.agnieszkajankowska.enauczyciel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.agnieszkajankowska.enauczyciel.model.SelectedValueContainer;
import pl.agnieszkajankowska.enauczyciel.model.Subject;
import pl.agnieszkajankowska.enauczyciel.service.SubjectService;

import java.util.List;

@RequestMapping("student/learn/subjects")
@Controller
public class SubjectLearningController {

    @Autowired
    private SubjectService service;

    @GetMapping
    String readSubject(Model model) {
        addModelAttribute(model);
        return "subjectsLearning";
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
