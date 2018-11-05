package pt.unl.fct.ciai.assemblers;

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
import pt.unl.fct.ciai.model.Comment;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.Review;

@Component
public class CommentResourceAssembler implements ResourceAssembler<Comment, Resource<Comment>> { //TODO resourcesAssembler

	@Override
	public Resource<Comment> toResource(Comment comment) {
		long cid = comment.getId();
		long pid = comment.getProposal().getId();
		return new Resource<>(comment,
				linkTo(methodOn(ProposalsController.class).getComment(pid, cid)).withSelfRel(),
				linkTo(methodOn(ProposalsController.class).getComments(pid)).withRel("comments"));
	}
	
	public Resources<Resource<Comment>> toResources(Iterable<? extends Comment> entities, Proposal proposal) {
		long cid = proposal.getId();
		List<Resource<Comment>> comments = 
				StreamSupport.stream(entities.spliterator(), false)
				.map(this::toResource)
				.collect(Collectors.toList());
		return new Resources<>(comments,
				linkTo(methodOn(ProposalsController.class).getComments(cid)).withSelfRel(),
				linkTo(methodOn(RootController.class).root()).withRel("root"));
	}
	
}
