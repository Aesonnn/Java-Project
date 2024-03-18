// package individ.site.controllers;

// import static org.mockito.Mockito.*;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.ui.Model;

// import individ.site.models.Department;
// import individ.site.models.Employee;
// import individ.site.models.Payroll;
// import individ.site.models.PayrollTax;
// import individ.site.models.Tax;
// import individ.site.repo.employeeRepository;
// import individ.site.repo.payrollRepository;
// import individ.site.repo.taxRepository;
// import individ.site.repo.payrolltaxRepository;
// import individ.site.controllers.EmployeeController;
// import individ.site.controllers.PayrollController;
// import static org.assertj.core.api.Assertions.assertThat;
// import java.util.Date;


// @SpringBootTest
// class SiteApplicationTests {

// 	@Test
// 	void contextLoads() {
// 	}

// 	@Mock
//     private employeeRepository employeeRepository;

//     @Mock
//     private payrollRepository payrollRepository;

// 	@Mock
// 	private payrolltaxRepository payrolltaxRepository;

//     @Mock
//     private taxRepository taxRepository;

//     @InjectMocks
//     private PayrollController payrollController;

// 	@InjectMocks
// 	private EmployeeController employeeController;

// 	// @Test
// 	// public void shouldCalculateNetPayWithTaxDeductions() {
// 	// 	// Test data
// 	// 	double grossPay = 1000.0;
// 	// 	List<Long> taxIds = Arrays.asList(1L, 2L); 

// 	// 	Employee employee = new Employee(); 
// 	// 	employee.setId(1L);  

// 	// 	Tax tax1 = new Tax(10.0, "Tax A", ""); // 10% deduction
// 	// 	Tax tax2 = new Tax(5.0, "Tax B", "");  // 5% deduction

// 	// 	// Mocking behavior
// 	// 	when(employeeRepository.existsById(anyLong())).thenReturn(true); 
// 	// 	when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee)); 
// 	// 	when(taxRepository.findById(1L)).thenReturn(Optional.of(tax1));
// 	// 	when(taxRepository.findById(2L)).thenReturn(Optional.of(tax2));

// 	// 	// Call the controller method
// 	// 	payrollController.payroll_post_add(null, taxIds, 1L, grossPay, null);

// 	// 	// Assertions (Example using AssertJ)
// 	// 	double expectedNetPay = grossPay * (1 - (tax1.getPercentRate() + tax2.getPercentRate()) / 100);
// 	// 	assertThat(employee.getPayrolls().get(0).getNetPay()).isEqualTo(expectedNetPay);
// 	// }

// 	@Test
// 	public void shouldCheckPayrollNetPay() {
// 		// Test data
// 		double grossPay = 1000.0;
// 		double taxRate = 10.0; // 10% deduction

// 		Date dob = new Date(); // Use the current date as the dob

// 		String fname = "fname";
// 		String lname = "lname";
// 		String email = "email";

// 		// String firstName, String lastName, Date dob, String email, Department department, Position position
// 		Employee employee = new Employee(fname, lname, dob, email); // Initially set to null

// 		// public Employee(String firstName, String lastName, Date dob, String email) {
// 		// 	this.firstName = firstName;
// 		// 	this.lastName = lastName;
// 		// 	this.dob = dob;
// 		// 	this.email = email;
// 		// 	this.department = null;
// 		// 	this.position = null;
// 		// }

// 		employee.setId(1L);

// 		Tax tax = new Tax(taxRate, "Tax A", "");

// 		// Mocking behavior
// 		when(employeeRepository.existsById(anyLong())).thenReturn(true);
// 		when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
// 		when(taxRepository.findById(anyLong())).thenReturn(Optional.of(tax));

// 		// Call the controller method
// 		payrollController.payroll_post_add(null, Collections.singletonList(1L), employee.getId(), grossPay, null);

// 		employee = employeeRepository.findById(1L).orElseThrow();
// 		// Get the payroll assigned to the employee
// 		Payroll payroll = employee.getPayrolls().get(0);

// 		// Calculate the expected net pay
// 		double expectedNetPay = grossPay * (1 - (taxRate / 100));

// 		// Assert the net pay of the payroll
// 		assertEquals(expectedNetPay, payroll.getNetPay());
// 	}

// 	// @Test
//     // void testPayrollPostAdd() {
//     //     // Arrange
//     //     String comments = "Test comments";
//     //     List<Long> taxIds = Collections.singletonList(1L);
//     //     Long employeeId = 1L;
//     //     Double grossPay = 1000.0;
//     //     Model model = mock(Model.class);
//     //     Employee employee = new Employee();
//     //     Payroll payroll = new Payroll(comments, null);
//     //     Tax tax = new Tax();
//     //     PayrollTax payrollTax = new PayrollTax(payroll, tax);

//     //     when(employeeRepository.existsById(employeeId)).thenReturn(true);
//     //     when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
//     //     when(taxRepository.findById(1L)).thenReturn(Optional.of(tax));
//     //     when(payrollRepository.save(any(Payroll.class))).thenReturn(payroll);
//     //     when(payrollRepository.save(any(PayrollTax.class))).thenReturn(payrollTax);

//     //     // Act
//     //     String result = payrollController.payroll_post_add(comments, taxIds, employeeId, grossPay, model);

//     //     // Assert
//     //     assertEquals("redirect:/payrolls", result);
//     //     verify(employeeRepository, times(1)).existsById(employeeId);
//     //     verify(employeeRepository, times(1)).findById(employeeId);
//     //     verify(taxRepository, times(1)).findById(1L);
//     //     verify(payrollRepository, times(1)).save(payroll);
//     //     verify(payrollRepository, times(1)).save(payrollTax);
//     //     verify(model, never()).addAttribute(eq("employeeError"), anyString());
//     // }
// }
