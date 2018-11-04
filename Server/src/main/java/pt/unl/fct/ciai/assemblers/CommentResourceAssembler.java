package pt.unl.fct.ciai.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.model.Comment;

@Component
public class CommentResourceAssembler implements ResourceAssembler<Comment, Resource<Comment>> {

	@Override
	public Resource<Comment> toResource(Comment comment) {
		long cid = comment.getId();
		long pid = comment.getProposal().getId();
		return new Resource<>(comment,
				linkTo(methodOn(ProposalsController.class).getComment(pid, cid)).withSelfRel(),
				linkTo(methodOn(ProposalsController.class).getComments(pid)).withRel("comments"));
	}
	
}
