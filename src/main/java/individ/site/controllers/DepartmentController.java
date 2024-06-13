package individ.site.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import individ.site.repo.departmentRepository;
import individ.site.repo.employeeRepository;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import individ.site.models.Department;
import individ.site.models.Employee;
import individ.site.models.User;
import jakarta.servlet.http.HttpSession;


@Controller
public class DepartmentController {
    
    @Autowired
    private departmentRepository departmentRepository;

    @Autowired
    private employeeRepository employeeRepository;

    @GetMapping("/departments")
    public String department_main(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            return "redirect:/login";
        }
        Iterable<Department> departments = departmentRepository.findAll();
        model.addAttribute("departments", departments);
        return "departments";
    }

    @GetMapping("/departments/add")
    public String department_add(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            return "redirect:/login";
        }
        return "departments-add";
    }


    @PostMapping("/departments/add")
    public String department_post_add(@RequestParam String title, 
                                    @RequestParam(required = false) String description, 
                                    Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            return "redirect:/login";
        }

        if (title.isBlank()) {
            model.addAttribute("titleError", "Title is required");
            return "departments-add"; // Re-render the form 
        }

        Department department = new Department(title, description);
        departmentRepository.save(department);
        return "redirect:/departments";
    }

    @GetMapping("/departments/{id}")
    public String department_details(@PathVariable(value = "id") long depId, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            return "redirect:/login";
        }
        try {
            if (departmentRepository.existsById(depId)) {
                Optional<Department> department = departmentRepository.findById(depId);
                ArrayList<Department> res = new ArrayList<>();
                department.ifPresent(res::add);
                model.addAttribute("dep", res);


                List<Employee> employees = employeeRepository.findByDepartmentId(depId);
                model.addAttribute("employees", employees);
                return "department-detail";
            } else {
                throw new Exception("Record does not exist");
            }
        } catch (Exception e) {
            return "redirect:/departments";
        }
    }

    @GetMapping("/departments/{id}/edit")
    public String department_edit(@PathVariable(value = "id") long depId, Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            return "redirect:/login";
        }

        if (departmentRepository.existsById(depId)) {
            Department department = departmentRepository.findById(depId).orElseThrow();
            model.addAttribute("dep", department);
            return "department-edit";
        } else {
            return "redirect:/departments";
        }
    }

    @PostMapping("/departments/{id}/edit")
    public String department_post_edit(@PathVariable(value = "id") long depId, @RequestParam String title, @RequestParam String description, Model model) {
        Department department = departmentRepository.findById(depId).orElseThrow();
        department.setTitle(title);
        department.setDescription(description);
        departmentRepository.save(department);
        return "redirect:/departments";
    }

    @PostMapping("/departments/{id}/delete")
    public String department_delete(@PathVariable(value = "id") long depId, Model model) {
        Department department = departmentRepository.findById(depId).orElseThrow();
        departmentRepository.delete(department);
        return "redirect:/departments";
    }


    
}
