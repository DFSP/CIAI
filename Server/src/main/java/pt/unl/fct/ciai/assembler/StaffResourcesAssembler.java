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
public class StaffResourcesAssembler implements SubResourceAssembler<User, Proposal, Resource<User>>,
        SubResourcesAssembler<User, Proposal, Resource<User>> {

    @Override
    public Resource<User> toResource(User user, Proposal proposal) {
        long pid = proposal.getId();
        long uid = user.getId();
        return new Resource<>(user,
                linkTo(methodOn(ProposalsController.class).getStaff(pid, uid)).withSelfRel(),
                linkTo(methodOn(UsersController.class).getProposals(uid, "")).withRel("proposals"),
                linkTo(methodOn(UsersController.class).getBids(uid, "")).withRel("bids"));
    }

    @Override
    public Resources<Resource<User>> toResources(Iterable<? extends User> entities, Proposal proposal) {
        long pid = proposal.getId();
        List<Resource<User>> users =
                StreamSupport.stream(entities.spliterator(), false)
                        .map(s -> this.toResource(s, proposal))
                        .collect(Collectors.toList());
        return new Resources<>(users,
                linkTo(methodOn(ProposalsController.class).getStaff(pid, "")).withSelfRel(),
                linkTo(methodOn(RootController.class).root()).withRel("root"));
    }

}
