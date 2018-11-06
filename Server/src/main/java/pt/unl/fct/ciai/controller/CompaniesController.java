package pt.unl.fct.ciai.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.MediaTypes;

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
@RequestMapping(value = "/partners", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class CompaniesController { //implements CompaniesApi { TODO

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
	public ResponseEntity<Resources<Resource<Company>>> getCompanies() { 
		// @RequestParam(required = false) String search) { // TODO search mesmo necessário? que campos?
		Iterable<Company> companies = companiesRepository.findAll();
		Resources<Resource<Company>> resources = companyAssembler.toResources(companies);
		return ResponseEntity.ok(resources);
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
	public ResponseEntity<Resource<Company>> getCompany(@PathVariable("id") long id) {
		Company company = findCompany(id);
		Resource<Company> resource = companyAssembler.toResource(company);
		return ResponseEntity.ok(resource);
	}

	@PutMapping(value = "/{id}")
	// @CanModifyCompany
	public ResponseEntity<?> updateCompany(@PathVariable("id") long id, @RequestBody Company newCompany) {
		if (id != newCompany.getId()) {
			throw new BadRequestException(String.format("Path id %d does not match company id %d", id, newCompany.getId()));
		}
		Company oldCompany = findCompany(id);
		companiesRepository.save(newCompany);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	// @CanDeleteCompany
	public ResponseEntity<?> deleteCompany(@PathVariable("id") long id) {
		Company company = findCompany(id);
		companiesRepository.delete(company);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}/employees")
	public ResponseEntity<Resources<Resource<Employee>>> getEmployees(@PathVariable("id") long id) { // TODO search mesmo necessário? que campos?	
		// @RequestParam(value = "search", required = false) String search)
		List<Resource<Employee>> employees = 
				findCompany(id).getEmployees()
				.stream()
				.map(employeeAssembler::toResource)
				.collect(Collectors.toList());
		Resources<Resource<Employee>> resources = new Resources<>(employees,
				linkTo(methodOn(CompaniesController.class).getEmployees(id)).withSelfRel());
		return ResponseEntity.ok(resources);
	}
	
	@PostMapping(value = "/{id}/employees")
	// @CanAddEmployee
	public ResponseEntity<Resource<Employee>> addEmployee(@PathVariable("id") long id, @RequestBody Employee employee) throws URISyntaxException {	
		Company company = findCompany(id);
		employee.setCompany(company);
		company.addEmployee(employee);
		companiesRepository.save(company); //TODO verificar se é necessário
		System.out.println("employe " +employee);
		Employee newEmployee = employeesRepository.save(employee);
		System.out.println("new employ " + newEmployee);
		Resource<Employee> resource = employeeAssembler.toResource(newEmployee);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping(value = "/{cid}/employees/{eid}")
	public ResponseEntity<Resource<Employee>> getEmployee(@PathVariable("cid") long cid, @PathVariable("eid") long eid) {
		Company company = findCompany(cid);
		Employee employee = findEmployee(eid);
		if (!employee.getCompany().equals(company)) {
			throw new BadRequestException(String.format("Employee id %d does not belong to company with id %d", eid, cid));
		}
		Resource<Employee> resource = employeeAssembler.toResource(employee);
		return ResponseEntity.ok(resource);
	}

	@PutMapping(value = "/{cid}/employees/{eid}")
	// @CanModifyEmployee
	public ResponseEntity<?> updateEmployee(@PathVariable("cid") long cid, @PathVariable("eid") long eid, @RequestBody Employee newEmployee) {
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

	@DeleteMapping(value = "/{cid}/employees/{eid}")
	//@CanDeleteEmployee
	public ResponseEntity<?> deleteEmployee(@PathVariable("cid") long cid, @PathVariable("eid") long eid) {		
		Company company = findCompany(cid);
		Employee employee = findEmployee(eid);
		if (employee.getCompany().getId() != company.getId()) {
			throw new BadRequestException(String.format("Employee id %d does not belong to company with id %d", eid, cid));
		}
		employeesRepository.delete(employee);
		return ResponseEntity.noContent().build();
	}

	private Company findCompany(long id) {
		return companiesRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Company with id %d not found.", id)));
	}

	private Employee findEmployee(long id) {
		return employeesRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Employee with id %d not found.", id)));
	}

}
