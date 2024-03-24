package individ.site.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// import individ.site.models.Department;
// import individ.site.models.Employee;
import individ.site.models.Payroll;
import individ.site.models.PayrollTax;
import individ.site.models.Tax;
import individ.site.models.Employee;
import individ.site.repo.payrollRepository;
import individ.site.repo.taxRepository;
import individ.site.repo.payrolltaxRepository;
import java.util.Optional;
import java.util.stream.Collectors;

import individ.site.repo.employeeRepository;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.StreamSupport;



@Controller
public class PayrollController {
    
    @Autowired
    private payrollRepository payrollRepository;

    @Autowired
    private payrolltaxRepository payrolltaxRepository;

    @Autowired
    private taxRepository taxRepository;

    @Autowired
    private employeeRepository employeeRepository;

    // @Autowired 
    // private taxRepository taxRepository;


    @GetMapping("/payrolls")
    public String getAllPayrolls(Model model, @RequestParam(required = false) String sortField) {
        Iterable<Payroll> payrolls;
        if (sortField != null) {
            switch (sortField) {
                case "id":
                    payrolls = payrollRepository.findAllByOrderById();
                    break;
                case "grossPay":
                    payrolls = payrollRepository.findAllByOrderByGrossPayDesc();
                    break;
                case "netPay":
                    payrolls = payrollRepository.findAllByOrderByNetPayDesc();
                    break;
                case "empfname":
                    payrolls = payrollRepository.findAllByOrderByEmployeeDesc();
                    break;
                case "taxesnum": 
                    // Map<Long, Long> payrollIdCounts = new HashMap<>();
                    // Iterable<PayrollTax> payrollTaxes = payrolltaxRepository.findAll();
                    // for (PayrollTax payrollTax : payrollTaxes) {
                    //     Long payrollId = payrollTax.getPayrollId();
                    //     if (payrollIdCounts.containsKey(payrollId)) {
                    //         payrollIdCounts.put(payrollId, payrollIdCounts.get(payrollId) + 1);
                    //     } else {
                    //         payrollIdCounts.put(payrollId, 1L);
                    //     }
                    // }
                    
                    // // Now we know which payroll has how many taxes
                    // // Sort the payrolls by the number of taxes
                    // List<Payroll> sortedPayrolls = new ArrayList<>();
                    // for (Map.Entry<Long, Long> entry : payrollIdCounts.entrySet()) {
                    //     Long payrollId = entry.getKey();
                    //     Long taxCount = entry.getValue();
                    //     Optional<Payroll> payroll = payrollRepository.findById(payrollId);
                    //     payroll.ifPresent(p -> {
                    //         p.setTaxCount(taxCount);
                    //         sortedPayrolls.add(p);
                    //     });
                    // }
                    payrolls = payrollRepository.findAll();
                    List<Payroll> payrollList = new ArrayList<>();
                    payrolls.forEach(payrollList::add);
                    Collections.sort(payrollList, (p1, p2) -> Integer.compare(p2.getPaytax().size(), p1.getPaytax().size()));
                    payrolls = payrollList;
                    break;
                default:
                    payrolls = payrollRepository.findAll();
            }
        } else {
            payrolls = payrollRepository.findAll();
        }
        model.addAttribute("payrolls", payrolls);
        return "payrolls";
    }

    @GetMapping("/payroll/{id}")
    public String payroll_details(@PathVariable(value = "id") long prId, Model model) {
        try {
            if (payrollRepository.existsById(prId)) {
                Payroll payroll = payrollRepository.findById(prId).orElseThrow();
                model.addAttribute("payrolls", payroll);

                // List<PayrollTax> payrollTaxes = res.get(0).getPayrollTax();
                // List<Tax> taxFields = new ArrayList<>();
                // for (PayrollTax payrollTax : payrollTaxes) {
                //     taxFields.add(payrollTax.getTax());
                // }


                List<PayrollTax> payrollTaxes = payrolltaxRepository.findByPayrollId(prId);
                List<Tax> taxFields = new ArrayList<>();
                for (PayrollTax payrollTax : payrollTaxes) {
                    taxFields.add(payrollTax.getTax());
                }

                model.addAttribute("taxes", taxFields);
                return "payroll-detail";
            } else {
                throw new Exception("Record does not exist");
            }
        } catch (Exception e) {
            return "redirect:/payrolls";
        }
    }

    @GetMapping("/payrolls/add")
    public String payroll_add(Model model) {
        model.addAttribute("taxes", taxRepository.findAll());
        return "payrolls-add-test";
    }


    Double net_pay;

    @PostMapping("/payrolls/add")
    public String payroll_post_add(@RequestParam String comments,
                                @RequestParam(required = false) List<Long> taxIds,
                                @RequestParam Long employeeId,
                                @RequestParam Double grossPay,
                                Model model) {

        // Define new payroll object
        Payroll payroll = new Payroll(comments, null); 
        
        if (employeeId == null) {
            model.addAttribute("employeeError", "Employee ID is required");
            return "payrolls-add"; 
        }

        if (!employeeRepository.existsById(employeeId)) {
            model.addAttribute("employeeError", "Employee with this ID does not exist");
            return "payrolls-add"; 
        }

        if(employeeId != null) {
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            employee.ifPresent(payroll::setEmployee);
        }

        payrollRepository.save(payroll); 

        if (taxIds != null) {
            for (Long taxId : taxIds) {
                Optional<Tax> tax = taxRepository.findById(taxId);
                tax.ifPresent(t -> {
                    PayrollTax payrollTax = new PayrollTax(payroll, t);
                    payrolltaxRepository.save(payrollTax);
                });
            }
        }

        net_pay = grossPay;
        payroll.setGrossPay(grossPay);
        payroll.setNetPay(net_pay);

        if (taxIds != null) {
            for (Long taxId : taxIds) {
                Optional<Tax> tax = taxRepository.findById(taxId);
                tax.ifPresent(t -> {    
                    t.deduct(payroll); // Apply deduction using the interface
                });
            }
        }

        payrollRepository.save(payroll);

        return "redirect:/payrolls"; // Redirect to a page listing payrolls
    }

    @GetMapping("/payroll/{id}/edit")
    public String payroll_get_edit(@PathVariable("id") Long id, Model model) {
        Optional<Payroll> optionalPayroll = payrollRepository.findById(id);
        if (optionalPayroll.isPresent()) {
            Payroll payroll = optionalPayroll.get();
            List<PayrollTax> payrollTaxes = payrolltaxRepository.findByPayrollId(payroll.getId()); // Fetch associated PayrollTax records
            model.addAttribute("payroll", payroll);
            model.addAttribute("taxes", taxRepository.findAll());  
            model.addAttribute("payrollTaxes", payrollTaxes); 
            return "payroll-edit";
        } else {
            return "redirect:/payrolls"; // Or a suitable error page
        }
    }
    
    @Transactional
    @PostMapping("/payroll/{id}/edit") // Modify existing post mapping for editing
    public String payroll_post_edit(@PathVariable("id") Long id,
                                    @RequestParam String comments,
                                    @RequestParam(required = false) List<Long> taxIds,
                                    @RequestParam Long employeeId,
                                    @RequestParam Double grossPay,
                                    Model model) {
        Payroll payroll = payrollRepository.findById(id).orElseThrow();
        
        if (employeeId == null) {
            model.addAttribute("employeeError", "Employee ID is required");
            return "payrolls-add"; 
        }

        if (!employeeRepository.existsById(employeeId)) {
            model.addAttribute("employeeError", "Employee with this ID does not exist");
            return "payrolls-add"; 
        }

        if(employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId).orElseThrow();
            payroll.setEmployee(employee);
            
        }

        payroll.setComments(comments);
        payroll.delete_all_paytax();
        payrolltaxRepository.deleteByPayrollId(payroll.getId()); // Delete all associated PayrollTax records
        if (taxIds != null) {
            for (Long taxId : taxIds) {
                Optional<Tax> tax = taxRepository.findById(taxId);
                tax.ifPresent(t -> {
                    PayrollTax payrollTax = new PayrollTax(payroll, t);
                    payrolltaxRepository.save(payrollTax);
                });
                ;
            }
        }

        net_pay = grossPay;
        payroll.setGrossPay(grossPay);
        payroll.setNetPay(net_pay);

        if (taxIds != null) {
            for (Long taxId : taxIds) {
                Optional<Tax> tax = taxRepository.findById(taxId);
                tax.ifPresent(t -> {    
                    t.deduct(payroll); // Apply deduction using the interface
                });
            }
        }
        
        // net_pay = grossPay;
        // if (taxIds != null) {
        //     for (Long taxId : taxIds) {
        //         Optional<Tax> tax = taxRepository.findById(taxId);
        //         tax.ifPresent(t -> {
        //             net_pay -= (grossPay * t.getPercentRate() / 100.0); 
        //         });
        //     }
        //     payroll.setGrossPay(grossPay);
        //     payroll.setNetPay(net_pay);
        // }
    
        payrollRepository.save(payroll);
        return "redirect:/payrolls"; // Redirect to a page listing payrolls
    }

    @PostMapping("/payroll/{id}/delete")
    public String payroll_delete(@PathVariable("id") Long id) {
        payrollRepository.deleteById(id);
        return "redirect:/payrolls"; // Redirect to a page listing payrolls
    }

    @GetMapping("/payrolls/filter")
    public String filterEmployees(Model model) {
        return "payrolls-filter"; 
    }

    @PostMapping("/payrolls/filter")
    public String filterEmployees(@RequestParam String filterBy, 
                                @RequestParam(required = false) Double filterValue, 
                                Model model) {

        
        if (filterValue == null) {
            // find all payrolls
            Iterable<Payroll> filteredPayroll = payrollRepository.findAll();
            model.addAttribute("filteredPayrolls", filteredPayroll);
            // model.addAttribute("error", "Please enter a value to filter by");
            return "payrolls-filter"; 
        }

        Long newFilterValue;
        List<Payroll> filteredPayroll = new ArrayList<>();
        switch (filterBy) {
            case "id":
                newFilterValue = filterValue.longValue();
                Optional<Payroll> payrolls = payrollRepository.findById(newFilterValue);
                payrolls.ifPresent(filteredPayroll::add); // Add if found
                break;
            case "employeeId":
                newFilterValue = filterValue.longValue();

                filteredPayroll = payrollRepository.findByEmployeeId(newFilterValue);
                break;
            case "netPay":
                filteredPayroll = payrollRepository.findByNetPay(filterValue);
                break;
            case "numTax":
                newFilterValue = filterValue.longValue();

                if (newFilterValue < 0) {
                    model.addAttribute("error", "Please enter a positive value");
                    return "payrolls-filter"; 
                }
                List<Payroll> filteredPayrolls = StreamSupport.stream(payrollRepository.findAll().spliterator(), false)
                                        .filter(p -> p.getPaytax().size() >= newFilterValue)
                                        .collect(Collectors.toList());
                
                filteredPayroll = filteredPayrolls;

            // List<Payroll> filteredPayrolls = StreamSupport.stream(payrollRepository.findAll().spliterator(), false)
            // .filter(p -> p.getPaytax().size() == taxCount)
            // .collect(Collectors.toList());
        }
        model.addAttribute("filteredPayrolls", filteredPayroll);
        return "payrolls-filter"; 
    

    }

    @GetMapping("/payrolls/summary")
    public String payroll_summary(Model model) {
        return "payrolls-summary"; 
    }

    @PostMapping("/payrolls/summary")
    public String filterPayrollsByTaxCount(@RequestParam(required = false) Integer taxCount, Model model) {

        if (taxCount == null || taxCount < 0) {
            model.addAttribute("error", "Please enter a positive value");
            return "payrolls-summary"; 
        }

        List<Payroll> filteredPayrolls = StreamSupport.stream(payrollRepository.findAll().spliterator(), false)
                                        .filter(p -> p.getPaytax().size() == taxCount)
                                        .collect(Collectors.toList());

        model.addAttribute("filteredPayrolls", filteredPayrolls); 
        model.addAttribute("taxCount", taxCount); 
        return "payrolls-summary"; // Name of your view file
    }
}


// @PostMapping("/payrolls/add")
//     public String payroll_post_add(@RequestParam String comments,
//                                 @RequestParam(required = false) List<Long> taxIds,
//                                 Model model) {

//         Payroll payroll = new Payroll(comments); 
//         payrollRepository.save(payroll); 

//         if (taxIds != null) {
//             for (Long taxId : taxIds) {
//                 Optional<Tax> tax = taxRepository.findById(taxId);
//                 tax.ifPresent(t -> {
//                     PayrollTax payrollTax = new PayrollTax(payroll, t);
//                     payrolltaxRepository.save(payrollTax);
//                 });
//             }
//         }

// if (taxIds != null) {
//     for (Long taxId : taxIds) {
//         Optional<Tax> tax = taxRepository.findById(taxId);
//         tax.ifPresent(t -> {
//             net_pay -= (grossPay * t.getPercentRate() / 100.0); 
//         });
//     }
//     payroll.setGrossPay(grossPay);
//     payroll.setNetPay(net_pay);
// }