package pt.unl.fct.ciai.employee;

import org.springframework.web.bind.annotation.*;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeesController {

    private final EmployeesRepository employees;

    public EmployeesController(EmployeesRepository employees) {
        this.employees = employees;
    }

    @GetMapping("")
    Iterable<Employee> getAllEmployees(@RequestParam (required = false) String search){
        return search == null ?
                employees.findAll() : employees.searchEmployees(search);
    }

    @GetMapping("{id}")
    Employee getEmployeeById(@PathVariable long id) {
        Optional<Employee> e1 = employees.findById(id);
        if(e1.isPresent()){
            return e1.get();
        }
        else throw new NotFoundException("Employee with id "+id+" does not exist.");
    }

    //Post ?

    @PutMapping("{id}")
    void updateEmployee(@PathVariable long id, @RequestBody Employee employee){

        if(employee.getId() == id){
            Optional<Employee> e1 = employees.findById(id);
            if(e1.isPresent())
                employees.save(employee);
            else throw new NotFoundException("Employee with id "+id+" does not exist.");

        }
        else throw new BadRequestException("Invalid request");
    }

    @DeleteMapping("{id}")
    void deleteEmployee(@PathVariable long id){
        Optional<Employee> e1 = employees.findById(id);
        if(e1.isPresent())
            employees.delete(e1.get());
        else throw new NotFoundException("Employee with id "+id+" does not exist.");

    }

}
