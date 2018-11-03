package pt.unl.fct.ciai.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.assemblers.CompanyResourceAssembler;
import pt.unl.fct.ciai.assemblers.EmployeeResourceAssembler;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;
import pt.unl.fct.ciai.repository.CompaniesRepository;
import pt.unl.fct.ciai.repository.EmployeesRepository;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/partners", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompaniesController { //implements CompaniesApi {

	private final CompaniesRepository companiesRepository;
	private final EmployeesRepository employeesRepository;

	private final CompanyResourceAssembler companyAssembler;
	private final EmployeeResourceAssembler employeeAssembler;

	public CompaniesController(CompaniesRepository companiesRepository, EmployeesRepository employeesRepository,
			CompanyResourceAssembler companyResourceAssembler, EmployeeResourceAssembler employeeResourceAssembler) {
		this.companiesRepository = companiesRepository;
		this.employeesRepository = employeesRepository;
		this.companyAssembler = companyResourceAssembler;
		this.employeeAssembler = employeeResourceAssembler;
	}

	@GetMapping
	public Resources<Resource<Company>> getCompanies() { //TODO search necessario? que campos?
		// @RequestParam(value = "search", required = false) String search)
		List<Resource<Company>> companies = 
				companiesRepository.findAll()
				.stream()
				.map(companyAssembler::toResource)
				.collect(Collectors.toList());
		return new Resources<>(companies,
				linkTo(methodOn(CompaniesController.class).getCompanies()).withSelfRel());
	}

	@PostMapping
	public ResponseEntity<Resource<Company>> addCompany(@RequestBody Company company) throws URISyntaxException {
		Company newCompany = companiesRepository.save(company);
		Resource<Company> resource = companyAssembler.toResource(newCompany);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);	
	}

	@GetMapping(value = "/{id}")
	public Resource<Company> getCompany(@PathVariable("id") long id) {
		Company company = findCompany(id);
		return companyAssembler.toResource(company);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateCompany(@PathVariable("id") long id, @RequestBody Company newCompany) {
		if (id != newCompany.getId()) {
			throw new BadRequestException(String.format("Path id %d does not match company id %d", id, newCompany.getId()));
		}
		Company oldCompany = findCompany(id);
		companiesRepository.save(newCompany);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteCompany(@PathVariable("id") long id) {
		Company deletedCompany = findCompany(id);
		companiesRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}/employees")
	public Resources<Resource<Employee>> getEmployees(@PathVariable("id") long id) { // TODO search mesmo necessário? que campos?	
		// @RequestParam(value = "search", required = false) String search)
		List<Resource<Employee>> employees = 
				findCompany(id).getEmployees()
				.stream()
				.map(employeeAssembler::toResource)
				.collect(Collectors.toList());
		return new Resources<>(employees,
				linkTo(methodOn(CompaniesController.class).getEmployees(id)).withSelfRel());	
	}

	@GetMapping(value = "/{cid}/employees/{eid}")
	public Resource<Employee> getEmployee(@PathVariable("id") long cid, @PathVariable long eid) {
		Company company = findCompany(cid);
		Employee employee = findEmployee(eid);
		if (!employee.getCompany().equals(company)) {
			throw new BadRequestException(String.format("Employee id %d does not belong to company with id %d", eid, cid));
		}
		return employeeAssembler.toResource(employee);
	}

	@PostMapping(value = "/{id}/employees")
	public ResponseEntity<Resource<Employee>> addEmployee(@PathVariable("id") long id, @RequestBody Employee employee) throws URISyntaxException {	
		Company company = findCompany(id);
		employee.setCompany(company);
		company.addEmployee(employee);
		companiesRepository.save(company); //TODO verificar se é necessário
		Employee newEmployee = employeesRepository.save(employee);
		Resource<Employee> resource = employeeAssembler.toResource(newEmployee);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@PutMapping(value = "/{pid}/employees/{eid}")
	public ResponseEntity<?> updateEmployee(@PathVariable("id") long cid, @PathVariable long eid, @RequestBody Employee newEmployee) {
		Company company = findCompany(cid);
		Employee oldEmployee = findEmployee(eid);
		if (newEmployee.getId() != eid) {
			throw new BadRequestException(String.format("Request body employee id %d and path paramenter eid %d don't match.", newEmployee.getId(), eid));
		}
		if (oldEmployee.getCompany().getId() != company.getId()) {
			throw new BadRequestException(String.format("Employee id %d does not belong to company with id %d", eid, cid));	
		}
		employeesRepository.save(newEmployee);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{pid}/employees/{eid}")
	public ResponseEntity<?> deleteEmployee(@PathVariable("id") long cid, @PathVariable long eid) {		
		Company company = findCompany(cid);
		Employee employee = findEmployee(eid);
		if (employee.getCompany().getId() != company.getId()) {
			throw new BadRequestException(String.format("Employee id %d does not belong to company with id %d", eid, cid));
		}
		employeesRepository.deleteById(eid);
		return ResponseEntity.noContent().build();
	}	

	private Company findCompany(long id) {
		return companiesRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Company with id %d not found", id)));
	}

	private Employee findEmployee(long id) {
		return employeesRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Employee with id %d not found", id)));
	}

}
