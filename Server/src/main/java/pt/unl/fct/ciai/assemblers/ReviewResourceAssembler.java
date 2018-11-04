package pt.unl.fct.ciai.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.model.Review;

@Component
public class ReviewResourceAssembler implements ResourceAssembler<Review, Resource<Review>> {

	@Override
	public Resource<Review> toResource(Review review) {
		long rid = review.getId();
		long pid = review.getProposal().getId();
		return new Resource<>(review,
				linkTo(methodOn(ProposalsController.class).getReview(pid, rid)).withSelfRel(),
				linkTo(methodOn(ProposalsController.class).getReviews(pid)).withRel("reviews"));
	}

}
