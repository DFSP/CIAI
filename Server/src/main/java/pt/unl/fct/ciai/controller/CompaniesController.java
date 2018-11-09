package pt.unl.fct.ciai.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.MediaTypes;

import pt.unl.fct.ciai.api.CompaniesApi;
import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.assembler.CompanyResourceAssembler;
import pt.unl.fct.ciai.assembler.EmployeeResourceAssembler;
import pt.unl.fct.ciai.exception.BadRequestException;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.service.CompaniesService;

@RestController
@RequestMapping(value = "/companies", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class CompaniesController implements CompaniesApi {

	private final CompaniesService companiesService;

	private final CompanyResourceAssembler companyAssembler;
	private final EmployeeResourceAssembler employeeAssembler;

	public CompaniesController(CompaniesService companiesService,
							   CompanyResourceAssembler companyAssembler, EmployeeResourceAssembler employeeAssembler) {
		this.companiesService = companiesService;
		this.companyAssembler = companyAssembler;
		this.employeeAssembler = employeeAssembler;
	}

	@GetMapping
	public ResponseEntity<Resources<Resource<Company>>> getCompanies(@RequestParam(required = false) String search) {
		Iterable<Company> companies = companiesService.getCompanies(search);
		Resources<Resource<Company>> resources = companyAssembler.toResources(companies);
		return ResponseEntity.ok(resources);
	}

	@PostMapping
	public ResponseEntity<Resource<Company>> addCompany(@RequestBody Company company) throws URISyntaxException {
		if (company.getId() > 0) {
			throw new BadRequestException("A new company has to have a non positive id.");
		}
		Company newCompany = companiesService.addCompany(company);
		Resource<Company> resource = companyAssembler.toResource(newCompany);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Resource<Company>> getCompany(@PathVariable("id") long id) {
		Company company = companiesService.getCompany(id).orElseThrow(() ->
				new NotFoundException(String.format("Company with id %d not found.", id)));
		Resource<Company> resource = companyAssembler.toResource(company);
		return ResponseEntity.ok(resource);
	}

	@PutMapping(value = "/{id}")
	// @CanModifyCompany
	public ResponseEntity<?> updateCompany(@PathVariable("id") long id, @RequestBody Company company) {
		if (company.getId() != id) {
			throw new BadRequestException(String.format("Company id %d and path id %d don't match", company.getId(), id));
		}
		companiesService.updateCompany(company);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	// @CanDeleteCompany
	public ResponseEntity<?> deleteCompany(@PathVariable("id") long id) {
		companiesService.deleteCompany(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}/employees")
	public ResponseEntity<Resources<Resource<Employee>>> getEmployees(
			@PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
		Company company = companiesService.getCompany(id).orElseThrow(() ->
				new NotFoundException(String.format("Company with id %d not found.", id)));
		Iterable<Employee> employees = companiesService.getEmployees(id, search);
		Resources<Resource<Employee>> resources = employeeAssembler.toResources(employees, company);
		return ResponseEntity.ok(resources);
	}
	
	@PostMapping(value = "/{id}/employees")
	// @CanAddEmployee
	public ResponseEntity<Resource<Employee>> addEmployee(@PathVariable("id") long id, @RequestBody Employee employee)
			throws URISyntaxException {
		Employee newEmployee = companiesService.addEmployee(id, employee);
		Resource<Employee> resource = employeeAssembler.toResource(newEmployee);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping(value = "/{cid}/employees/{eid}")
	public ResponseEntity<Resource<Employee>> getEmployee(@PathVariable("cid") long cid, @PathVariable("eid") long eid) {
		Employee employee = companiesService.getEmployee(cid, eid).orElseThrow(() ->
				new BadRequestException(String.format("Employee with id %d is not part of company with id %d", eid, cid)));
		Resource<Employee> resource = employeeAssembler.toResource(employee);
		return ResponseEntity.ok(resource);
	}

	@PutMapping(value = "/{cid}/employees/{eid}")
	// @CanModifyEmployee
	public ResponseEntity<?> updateEmployee(@PathVariable("cid") long cid, @PathVariable("eid") long eid, @RequestBody Employee employee) {
		if (employee.getId() != eid) {
			throw new BadRequestException(String.format("Employee id %d and path id %d don't match.", employee.getId(), eid));
		}
		companiesService.updateEmployee(cid, employee);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{cid}/employees/{eid}")
	//@CanDeleteEmployee
	public ResponseEntity<?> deleteEmployee(@PathVariable("cid") long cid, @PathVariable("eid") long eid) {
		companiesService.deleteEmployee(cid, eid);
		return ResponseEntity.noContent().build();
	}

}
