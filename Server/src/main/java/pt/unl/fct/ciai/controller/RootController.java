package pt.unl.fct.ciai.controller;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


//@RestController
@RequestMapping(value = "/", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class RootController {

	@GetMapping
	public ResponseEntity<ResourceSupport> root() {
		ResourceSupport resourceSupport = new ResourceSupport();
		resourceSupport.add(linkTo(methodOn(RootController.class).root()).withSelfRel());
		resourceSupport.add(linkTo(methodOn(UsersController.class).getUsers("")).withRel("users"));
		resourceSupport.add(linkTo(methodOn(CompaniesController.class).getCompanies("")).withRel("companies"));
		resourceSupport.add(linkTo(methodOn(ProposalsController.class).getProposals("")).withRel("proposals"));
		return ResponseEntity.ok(resourceSupport);
	}

}
