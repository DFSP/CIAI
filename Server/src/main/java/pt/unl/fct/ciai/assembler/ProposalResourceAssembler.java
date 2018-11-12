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

import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.controller.RootController;
import pt.unl.fct.ciai.controller.UsersController;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.Section;
import pt.unl.fct.ciai.model.User;

@Component
public class ProposalResourceAssembler implements ResourceAssembler<Proposal, Resource<Proposal>>,
		ResourcesAssembler<Proposal, Resource<Proposal>> {

	@Override
	public Resource<Proposal> toResource(Proposal proposal) {
		long id = proposal.getId();
		long uid = proposal.getProposer().getId();
		return new Resource<>(proposal,
				linkTo(methodOn(ProposalsController.class).getProposal(id)).withSelfRel(),
				linkTo(methodOn(ProposalsController.class).getProposals("")).withRel("proposals"),
				linkTo(methodOn(ProposalsController.class).getStaff(id, "")).withRel("staff"),
				linkTo(methodOn(ProposalsController.class).getMembers(id, "")).withRel("members"),
				linkTo(methodOn(ProposalsController.class).getSections(id, "")).withRel("sections"),
				linkTo(methodOn(ProposalsController.class).getReviews(id, "")).withRel("reviews"),
				linkTo(methodOn(ProposalsController.class).getComments(id, "")).withRel("comments"),
				linkTo(methodOn(ProposalsController.class).getReviewBids(id, "")).withRel("reviewBid"),
				linkTo(methodOn(ProposalsController.class).getReviewers(id, "")).withRel("reviewers"),
				linkTo(methodOn(UsersController.class).getUser(uid)).withRel("proposer"));
	}

	@Override
	public Resources<Resource<Proposal>> toResources(Iterable<? extends Proposal> entities) {
		List<Resource<Proposal>> resources = 
				StreamSupport.stream(entities.spliterator(), false)
				.map(this::toResource)
				.collect(Collectors.toList());
		return new Resources<>(resources,
				linkTo(methodOn(ProposalsController.class).getProposals("")).withSelfRel(),
				linkTo(methodOn(RootController.class).root()).withRel("root"));
	}

}
