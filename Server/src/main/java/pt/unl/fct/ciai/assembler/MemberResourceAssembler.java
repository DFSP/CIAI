package pt.unl.fct.ciai.assembler;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;
import pt.unl.fct.ciai.controller.CompaniesController;
import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.controller.RootController;
import pt.unl.fct.ciai.controller.UsersController;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.model.Proposal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class MemberResourceAssembler implements SubResourceAssembler<Employee, Proposal, Resource<Employee>>,
        SubResourcesAssembler<Employee, Proposal, Resource<Employee>> {

    @Override
    public Resource<Employee> toResource(Employee member, Proposal proposal) {
        long pid = proposal.getId();
        long mid = member.getId();
        long cid = member.getCompany().getId();
        return new Resource<Employee>(member,
                linkTo(methodOn(ProposalsController.class).getMember(pid, mid)).withSelfRel(),
                linkTo(methodOn(UsersController.class).getProposals(mid, "")).withRel("proposals"),
                linkTo(methodOn(UsersController.class).getBids(mid, "")).withRel("bids"),
                linkTo(methodOn(CompaniesController.class).getCompany(cid)).withRel("company"));
    }

    @Override
    public Resources<Resource<Employee>> toResources(Iterable<? extends Employee> entities, Proposal proposal) {
        long pid = proposal.getId();
        List<Resource<Employee>> employees =
                StreamSupport.stream(entities.spliterator(), false)
                        .map(e -> this.toResource(e, proposal))
                        .collect(Collectors.toList());
        return new Resources<>(employees,
                linkTo(methodOn(ProposalsController.class).getMembers(pid, "")).withSelfRel(),
                linkTo(methodOn(RootController.class).root()).withRel("root"));
    }

}
