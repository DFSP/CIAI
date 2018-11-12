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
import pt.unl.fct.ciai.model.Review;

@Component
public class ReviewResourcesAssembler implements ResourceAssembler<Review, Resource<Review>>,
		SubResourcesAssembler<Review, Proposal, Resource<Review>> {

	@Override
	public Resource<Review> toResource(Review review) {
		long rid = review.getId();
		long pid = review.getProposal().getId();
		long aid = review.getAuthor().getId();
		return new Resource<>(review,
				linkTo(methodOn(ProposalsController.class).getReview(pid, rid)).withSelfRel(),
				linkTo(methodOn(ProposalsController.class).getReviews(pid, "")).withRel("reviews"),
				linkTo(methodOn(UsersController.class).getUser(aid)).withRel("author"),
				linkTo(methodOn(ProposalsController.class).getProposal(pid)).withRel("proposal"));
	}

	@Override
	public Resources<Resource<Review>> toResources(Iterable<? extends Review> entities, Proposal proposal) {
		long cid = proposal.getId();
		List<Resource<Review>> reviews = 
				StreamSupport.stream(entities.spliterator(), false)
				.map(this::toResource)
				.collect(Collectors.toList());
		return new Resources<>(reviews,
				linkTo(methodOn(ProposalsController.class).getReviews(cid, "")).withSelfRel(),
				linkTo(methodOn(RootController.class).root()).withRel("root"));
	}
	
}
