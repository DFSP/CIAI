package pt.unl.fct.ciai.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.MediaTypes;

import pt.unl.fct.ciai.exception.BadRequestException;
import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.assembler.CompanyResourceAssembler;
import pt.unl.fct.ciai.assembler.EmployeeResourcesAssembler;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.security.*;
import pt.unl.fct.ciai.service.CompaniesService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/companies", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class CompaniesController {

	private final CompaniesService companiesService;

	private final CompanyResourceAssembler companyAssembler;
	private final EmployeeResourcesAssembler employeeAssembler;

	public CompaniesController(CompaniesService companiesService,
                               CompanyResourceAssembler companyAssembler, EmployeeResourcesAssembler employeeAssembler) {
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
	@CanAddCompany
	public ResponseEntity<Resource<Company>> addCompany(@Valid @RequestBody Company company) throws URISyntaxException {
		if (company.getId() > 0) {
            throw new BadRequestException(String.format("Expected non negative company id, instead got %d", company.getId()));
        }
		Company newCompany = companiesService.addCompany(company);
		Resource<Company> resource = companyAssembler.toResource(newCompany);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Resource<Company>> getCompany(@PathVariable("id") long id) {
		Company company = getCompanyIfPresent(id);
		Resource<Company> resource = companyAssembler.toResource(company);
		return ResponseEntity.ok(resource);
	}

	@PutMapping(value = "/{id}")
	@CanModifyCompany
	public ResponseEntity<?> updateCompany(@PathVariable("id") long id, @RequestBody Company company) {
		if (id != company.getId()) {
			throw new BadRequestException(String.format("Path id %d and company id %d don't match.", id, company.getId()));
		}
		companiesService.updateCompany(company);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	@CanDeleteCompany
	public ResponseEntity<?> deleteCompany(@PathVariable("id") long id) {
		companiesService.deleteCompany(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}/employees")
	public ResponseEntity<Resources<Resource<Employee>>> getEmployees(
			@PathVariable("id") long id, @RequestParam(value = "search", required = false) String search) {
		Company company = getCompanyIfPresent(id);
		Iterable<Employee> employees = companiesService.getEmployees(id, search);
		Resources<Resource<Employee>> resources = employeeAssembler.toResources(employees, company);
		return ResponseEntity.ok(resources);
	}
	
	@PostMapping(value = "/{id}/employees")
	@CanAddEmployee
	public ResponseEntity<Resource<Employee>> addEmployee(
			@PathVariable("id") long id, @Valid @RequestBody Employee employee) throws URISyntaxException {
		if (employee.getId() > 0) {
			throw new BadRequestException(String.format("Expected non negative employee id, instead got %d", employee.getId()));
		}
		Employee newEmployee = companiesService.addEmployee(id, employee);
		Resource<Employee> resource = employeeAssembler.toResource(newEmployee);
		return ResponseEntity
				.created(new URI(resource.getId().expand().getHref()))
				.body(resource);
	}

	@GetMapping(value = "/{cid}/employees/{eid}")
	public ResponseEntity<Resource<Employee>> getEmployee(@PathVariable("cid") long cid, @PathVariable("eid") long eid) {
		Employee employee = companiesService.getEmployee(cid, eid).orElseThrow(() ->
				new NotFoundException(String.format("Employee with id %d is not part of company with id %d", eid, cid)));
		Resource<Employee> resource = employeeAssembler.toResource(employee);
		return ResponseEntity.ok(resource);
	}

	@PutMapping(value = "/{cid}/employees/{eid}")
	@CanModifyEmployee
	public ResponseEntity<?> updateEmployee(
			@PathVariable("cid") long cid, @PathVariable("eid") long eid, @RequestBody Employee employee) {
		if (eid != employee.getId()) {
			throw new BadRequestException(String.format("Path id %d and employee id %d don't match.", eid, employee.getId()));
		}
		companiesService.updateEmployee(cid, employee);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{cid}/employees/{eid}")
	@CanDeleteEmployee
	public ResponseEntity<?> deleteEmployee(@PathVariable("cid") long cid, @PathVariable("eid") long eid) {
		companiesService.deleteEmployee(cid, eid);
		return ResponseEntity.noContent().build();
	}

	private Company getCompanyIfPresent(long id) {
		return companiesService.getCompany(id).orElseThrow(() ->
				new NotFoundException(String.format("Company with id %d not found.", id)));
	}

}
