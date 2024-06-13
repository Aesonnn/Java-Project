package individ.site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import individ.site.models.User;
import individ.site.repo.employeeRepository;
import individ.site.repo.payrollRepository;
// import individ.site.repo.payrolltaxRepository;
import individ.site.repo.taxRepository;


@Controller
public class MainController {

    @Autowired
    private payrollRepository payrollRepository;

    @Autowired
    private taxRepository taxRepository;

    @Autowired
    private employeeRepository employeeRepository;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            return "redirect:/login"; // Redirect to login page if user not authenticated
        }
        model.addAttribute("payrolls", payrollRepository.findAll());

        model.addAttribute("taxes", taxRepository.findAll());

        model.addAttribute("employees", employeeRepository.findAll());
        return "home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "payrolls";
    }
    

    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            return "redirect:/login"; // Redirect to login page if user not authenticated
        }
        model.addAttribute("payrolls", payrollRepository.findAll());

        model.addAttribute("taxes", taxRepository.findAll());

        model.addAttribute("employees", employeeRepository.findAll());
        
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About this system");
        return "about";
    }
}
