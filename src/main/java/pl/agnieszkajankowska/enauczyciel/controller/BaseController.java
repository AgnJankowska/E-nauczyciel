package pl.agnieszkajankowska.enauczyciel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.agnieszkajankowska.enauczyciel.security.User;

@Controller
public class BaseController {

    @GetMapping("/")
    String showIndexSite(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @GetMapping("/student")
    String showStudentConsole() {
        return "student";
    }

    @GetMapping("/teacher")
    String showTeacherConsole() {
        return "teacher";
    }

    @GetMapping("/login")
    String showLoginConsole() {
        return "login";
    }

    @GetMapping("/logout")
    String showLogoutConsole() {
        return "index";
    }
}
