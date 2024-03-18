package individ.site.repo;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import individ.site.models.Employee;

public interface employeeRepository extends CrudRepository<Employee, Long> {
    List<Employee> findByLastName(String lastName);
    List<Employee> findByFirstName(String firstName);
    List<Employee> findAllByOrderByDepartment();
    List<Employee> findByDepartmentId(Long id);
    List<Employee> findAllByOrderByFirstName();
    List<Employee> findAllByDepartmentIsNotNullOrderByDepartment();
    List<Employee> findByFirstNameContaining(String keyword);
    List<Employee> findByLastNameContaining(String keyword);
    List<Employee> findByPositionId(Long id);
    List<Employee> findAllByOrderByPayrollsDesc();
} 
