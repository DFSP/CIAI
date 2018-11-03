package pt.unl.fct.ciai.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;
import pt.unl.fct.ciai.repository.CompaniesRepository;
import pt.unl.fct.ciai.repository.EmployeesRepository;

@RestController
@RequestMapping("/partners")
public class CompaniesController { //implements CompaniesApi {
	
	private final CompaniesRepository companies;
	private final EmployeesRepository employees;

	public CompaniesController(CompaniesRepository companies, EmployeesRepository employees) {
		this.companies = companies;
		this.employees = employees;
	}
	
	@GetMapping
	public Iterable<Company> getCompanies(@RequestParam(value = "search", required = false) String search) {
		return search == null ? companies.findAll() : companies.searchCompanies(search);
	}

	@PostMapping
	public void addCompany(@RequestBody Company company) {
		companies.save(company);
	}

	@GetMapping(value = "/{id}")
	public Company getCompany(@PathVariable("id") long id) {
		return findCompanyOrThrowException(id);
	}

	@PutMapping(value = "/{id}")
	public void updateCompany(@PathVariable("id") long id, @RequestBody Company company) {
		if (id == company.getId()) {
			findCompanyOrThrowException(id);
			companies.save(company);
		}
		else {
			throw new BadRequestException(String.format("Path id %d does not match company id %d", id, company.getId()));
		}
	}

	@DeleteMapping(value = "/{id}")
	public void deleteCompany(@PathVariable("id") long id) {
		findCompanyOrThrowException(id);
		companies.deleteById(id);
	}
	
	@GetMapping(value = "/{pid}/employees/{eid}")
	public Employee getOneCompanyEmployee(@PathVariable("id") long pid, @PathVariable long eid) {
		Company company = findCompanyOrThrowException(pid);
		Employee employee = findEmployeeOrThrowException(eid);
		if (!employee.getCompany().equals(company))
			throw new BadRequestException(String.format("Employee id %d does not belong to company with id %d", eid, pid));
		return employee;
	}
	
	@PutMapping(value = "/{pid}/employees/{eid}")
	public void updateOneCompanyEmployee(@PathVariable("id") long pid, @PathVariable long eid, @RequestBody Employee employee) {
		findCompanyOrThrowException(pid); // Just to make sure the company exists. If not, throw exception.
		Employee employeeFound = findEmployeeOrThrowException(eid);
		if (employee.getId() == eid) {
			if (employeeFound.getCompany().getId() == pid) {
				employees.save(employee);
			} else throw new BadRequestException(String.format("Employee id %d does not belong to company with id %d", eid, pid));
		} else throw new BadRequestException("Invalid request: Request body employee eid and path paramenter eid don't match.");
	}
	
	@DeleteMapping(value = "/{pid}/employees/{eid}")
	public void deleteOneCompanyEmployee(@PathVariable("id") long pid, @PathVariable long eid) {
		findCompanyOrThrowException(pid); // Just to make sure the company exists. If not, throw exception.
		Employee employeeFound = findEmployeeOrThrowException(eid);
		if (employeeFound.getCompany().getId() == pid) {
			employees.deleteById(eid);
		} else throw new BadRequestException(String.format("Employee id %d does not belong to company with id %d", eid, pid));
	}	
	
	@PostMapping(value = "/{id}/employees")
	public void addOneCompanyEmployee(@PathVariable("id") long id, @RequestBody Employee employee) {
		Company company = findCompanyOrThrowException(id);
		employee.setCompany(company);
		company.addEmployee(employee);
		companies.save(company);
	}
	
	@GetMapping(value = "/{id}/employees")
	public Iterable<Employee> getCompanyEmployees(@PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
		return search == null ? findCompanyOrThrowException(id).getEmployees() : companies.searchEmployees(search);
	}
	
	private Company findCompanyOrThrowException(long id) {
		return companies.findById(id).orElseThrow(() -> new NotFoundException(String.format("Company with id %d not found", id)));
	}
	
	private Employee findEmployeeOrThrowException(long id) {
		return employees.findById(id).orElseThrow(() -> new NotFoundException(String.format("Employee with id %d not found", id)));
	}

}
