package pt.unl.fct.ciai.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.controller.CompaniesController;
import pt.unl.fct.ciai.model.Employee;

@Component
public class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {

	@Override
	public Resource<Employee> toResource(Employee employee) {
		long eid = employee.getId();
		long cid = employee.getCompany().getId();
		return new Resource<>(employee,
			linkTo(methodOn(CompaniesController.class).getEmployee(cid, eid)).withSelfRel(),
			linkTo(methodOn(CompaniesController.class).getEmployees(cid)).withRel("employees"));
	}
	
}
