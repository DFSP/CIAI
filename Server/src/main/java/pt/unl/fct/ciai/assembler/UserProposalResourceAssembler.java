package pt.unl.fct.ciai.assembler;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;
import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.controller.RootController;
import pt.unl.fct.ciai.controller.UsersController;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserProposalResourceAssembler implements
        SubResourceAssembler<Proposal, User, Resource<Proposal>>,
        SubResourcesAssembler<Proposal, User, Resource<Proposal>> {

    @Override
    public Resource<Proposal> toResource(Proposal proposal, User user) {
        long pid = proposal.getId();
        long uid = user.getId();
        return new Resource<>(proposal,
                linkTo(methodOn(UsersController.class).getProposal(uid, pid)).withSelfRel(),
                linkTo(methodOn(UsersController.class).getProposals(uid,"")).withRel("proposals"),
                linkTo(methodOn(ProposalsController.class).getStaff(pid, "")).withRel("staff"),
                linkTo(methodOn(ProposalsController.class).getMembers(pid, "")).withRel("members"),
                linkTo(methodOn(ProposalsController.class).getSections(pid, "")).withRel("sections"),
                linkTo(methodOn(ProposalsController.class).getReviews(pid, "")).withRel("reviews"),
                linkTo(methodOn(ProposalsController.class).getComments(pid, "")).withRel("comments"),
                linkTo(methodOn(ProposalsController.class).getReviewBids(pid, "")).withRel("reviewBids"),
                linkTo(methodOn(ProposalsController.class).getReviewers(pid, "")).withRel("reviewers"),
                linkTo(methodOn(UsersController.class).getUser(proposal.getProposer().getId())).withRel("proposer"));
    }

    @Override
    public Resources<Resource<Proposal>> toResources(Iterable<? extends Proposal> entities, User user) {
        List<Resource<Proposal>> resources =
                StreamSupport.stream(entities.spliterator(), false)
                        .map(p -> this.toResource(p, user))
                        .collect(Collectors.toList());
        return new Resources<>(resources,
                linkTo(methodOn(UsersController.class).getProposals(user.getId(), "")).withSelfRel(),
                linkTo(methodOn(RootController.class).root()).withRel("root"));
    }
}
