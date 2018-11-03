package pt.unl.fct.ciai.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.controller.CompaniesController;
import pt.unl.fct.ciai.model.Company;

@Component
public class CompanyResourceAssembler implements ResourceAssembler<Company, Resource<Company>> {

	@Override
	public Resource<Company> toResource(Company company) {
		return new Resource<>(company,
				linkTo(methodOn(CompaniesController.class).getCompany(company.getId())).withSelfRel(),
				linkTo(methodOn(CompaniesController.class).getCompanies()).withRel("companies"),
				linkTo(methodOn(CompaniesController.class).getEmployees(company.getId())).withRel("employees"));
	}

}
