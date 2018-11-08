package pt.unl.fct.ciai.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.controller.CompaniesController;
import pt.unl.fct.ciai.controller.RootController;
import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;

@Component
public class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {

	@Override
	public Resource<Employee> toResource(Employee employee) {
		long eid = employee.getId();
		long cid = employee.getCompany().get().getId(); //TODO o que fazer quando retorna null?
		return new Resource<>(employee,
			linkTo(methodOn(CompaniesController.class).getEmployee(cid, eid)).withSelfRel(),
			linkTo(methodOn(CompaniesController.class).getEmployees(cid)).withRel("employees"));
	}	
	
	public Resources<Resource<Employee>> toResources(Iterable<? extends Employee> entities, Company company) {
		long cid = company.getId();
		List<Resource<Employee>> employees = 
				StreamSupport.stream(entities.spliterator(), false)
				.map(this::toResource)
				.collect(Collectors.toList());
		return new Resources<>(employees,
				linkTo(methodOn(CompaniesController.class).getEmployees(cid)).withSelfRel(),
				linkTo(methodOn(RootController.class).root()).withRel("root"));
	}
	
}
