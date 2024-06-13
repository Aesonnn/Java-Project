package individ.site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import individ.site.models.User;
import individ.site.repo.UserRepository;
// import individ.site.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;




@Controller
@SessionAttributes("loggedInUser")
public class UserController {


    @Autowired
    private UserRepository userRepositoryrepo;

    @GetMapping("/login")
    public String login(Model model) {
        return "login"; // Display login page
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model){

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return "redirect:/login";
        }

        User logged_user = userRepositoryrepo.findByUsernameAndPassword(username, password);

        if(logged_user != null){
            model.addAttribute("loggedInUser", logged_user);
            return "redirect:/main"; // Redirect to main page after successful login
        } else {
            // add error atributte to the model
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                                    @RequestParam String password,
                                    @RequestParam String firstName,
                                    @RequestParam String lastName,
                                    Model model) {
        // if any field is null or empty, return to the registration page
        if (username == null || username.isEmpty() || password == null || password.isEmpty() || firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            return "redirect:/register";
        }

        // Check if user with such username already exists
        User existingUser = userRepositoryrepo.findByUsername(username);

        if (existingUser != null) {
            // add error atributte to the model
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        
        // Create a new user object
        User user = new User(username, password, firstName, lastName);
        userRepositoryrepo.save(user);
        return "redirect:/login"; // Redirect to login page after registration
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.removeAttribute("loggedInUser");
            session.invalidate();
        }
        // Set loggedInUser attribute to null in the model
        model.addAttribute("loggedInUser", null);
        return "redirect:/"; // Redirect to the login page after logging out
    }
    
    @GetMapping("/users")
    public String users(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login page if user not authenticated
        }
        
        // Get the logged-in user's information
        model.addAttribute("user", loggedInUser);
        
        return "users"; // Display user info page
    }
}
