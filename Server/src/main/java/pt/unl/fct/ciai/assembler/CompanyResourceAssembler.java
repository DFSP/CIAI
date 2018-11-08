package pt.unl.fct.ciai.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.Resources;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.controller.CompaniesController;
import pt.unl.fct.ciai.controller.RootController;
import pt.unl.fct.ciai.model.Company;

@Component
public class CompanyResourceAssembler implements ResourcesAssembler<Company, Resource<Company>> {

	@Override
	public Resource<Company> toResource(Company company) {
		return new Resource<>(company,
				linkTo(methodOn(CompaniesController.class).getCompany(company.getId())).withSelfRel(),
				linkTo(methodOn(CompaniesController.class).getCompanies()).withRel("companies"),
				linkTo(methodOn(CompaniesController.class).getEmployees(company.getId())).withRel("employees"));
	}
	
	@Override
	public Resources<Resource<Company>> toResources(Iterable<? extends Company> entities) {
		List<Resource<Company>> resources = 
				StreamSupport.stream(entities.spliterator(), false)
				.map(this::toResource)
				.collect(Collectors.toList());
		return new Resources<>(resources,
				linkTo(methodOn(CompaniesController.class).getCompanies()).withSelfRel(),
				linkTo(methodOn(RootController.class).root()).withRel("root"));
	}
	
	
}
