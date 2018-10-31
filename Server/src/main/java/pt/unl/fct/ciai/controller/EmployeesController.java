package pt.unl.fct.ciai.controller;

import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.repository.EmployeesRepository;

@RestController
@RequestMapping(value = "/employees")
public class EmployeesController {

    private final EmployeesRepository employees;

    public EmployeesController(EmployeesRepository employees) {
        this.employees = employees;
    }

    @GetMapping
    public Iterable<Employee> getEmployees(@RequestParam(value = "search", required = false) String search) {
        return search == null ? employees.findAll() : employees.searchEmployee(search);
    }

    @GetMapping(value = "/{id}")
    public Employee getEmployee(@PathVariable("id") Long id) {
        return findEmployeeOrThrowException(id);
    }

    @PutMapping(value = "/{id}")
    public void updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        if (id.equals(employee.getId())) {
            findEmployeeOrThrowException(id);
            employees.save(employee);
        } else {
            throw new BadRequestException(String.format("Path id %d does not match employee id %d", id, employee.getId()));
        }
    }

    @DeleteMapping(value = "/{id}")
    public void deleteEmployee(@PathVariable("id") Long id) {
        findEmployeeOrThrowException(id);
        employees.deleteById(id);
    }

    private Employee findEmployeeOrThrowException(Long id) {
        return employees.findById(id).orElseThrow(() -> new NotFoundException(String.format("Employee with id %d does not exist", id)));
    }

}
