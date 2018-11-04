package pt.unl.fct.ciai.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.model.Proposal;

@Component
public class ProposalResourceAssembler implements ResourcesAssembler<Proposal, Resource<Proposal>> {

	@Override
	public Resource<Proposal> toResource(Proposal proposal) {
		return new Resource<>(proposal,
				linkTo(methodOn(ProposalsController.class).getProposal(proposal.getId())).withSelfRel(),
				linkTo(methodOn(ProposalsController.class).getProposals()).withRel("proposals"),
				linkTo(methodOn(ProposalsController.class).getReviews(proposal.getId())).withRel("reviews"),
				linkTo(methodOn(ProposalsController.class).getComments(proposal.getId())).withRel("comments"),
				linkTo(methodOn(ProposalsController.class).getSections(proposal.getId())).withRel("sections"),
				linkTo(methodOn(ProposalsController.class).getBiddingUsers(proposal.getId())).withRel("biddings"));
	}

	@Override
	public Resources<Resource<Proposal>> toResources(Iterable<? extends Proposal> entities) {
		List<Resource<Proposal>> resources = 
				StreamSupport.stream(entities.spliterator(), false)
				.map(this::toResource)
				.collect(Collectors.toList());
		return new Resources<>(resources,
				linkTo(methodOn(ProposalsController.class).getProposals()).withSelfRel());
	}

}
