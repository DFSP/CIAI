package pt.unl.fct.ciai.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.controller.RootController;
import pt.unl.fct.ciai.controller.UsersController;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.User;

@Component
public class UserResourceAssembler implements ResourcesAssembler<User, Resource<User>> {


	@Override
	public Resource<User> toResource(User user) {
		return new Resource<>(user,
				linkTo(methodOn(UsersController.class).getUser(user.getId())).withSelfRel(),
				linkTo(methodOn(UsersController.class).getUsers("")).withRel("users"),
				linkTo(methodOn(UsersController.class).getApproverInProposals(user.getId(), "")).withRel("approverInProposals"));
	}

	@Override
	public Resources<Resource<User>> toResources(Iterable<? extends User> entities) {
		List<Resource<User>> resources = 
				StreamSupport.stream(entities.spliterator(), false)
				.map(this::toResource)
				.collect(Collectors.toList());
		return new Resources<>(resources,
				linkTo(methodOn(UsersController.class).getUsers("")).withSelfRel(),
				linkTo(methodOn(RootController.class).root()).withRel("root"));
	}

	public Resources<Resource<User>> toResources(Iterable<? extends User> entities, Proposal proposal) {
		long pid = proposal.getId();
		List<Resource<User>> users = 
				StreamSupport.stream(entities.spliterator(), false)
				.map(this::toResource)
				.collect(Collectors.toList());
		return new Resources<>(users,
				linkTo(methodOn(ProposalsController.class).getReviewBiddings(pid, "")).withSelfRel(),
				linkTo(methodOn(RootController.class).root()).withRel("root"));
	}
	
}
