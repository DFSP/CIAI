package pt.unl.fct.ciai.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.controller.RootController;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.Section;

@Component
public class SectionResourceAssembler implements SubResourcesAssembler<Section, Proposal, Resource<Section>> {

	@Override
	public Resource<Section> toResource(Section section) {
<<<<<<< HEAD
		long sid = section.getId();
		long pid = section.getProposal().getId();
		return new Resource<>(section,
				linkTo(methodOn(ProposalsController.class).getSection(pid, sid)).withSelfRel(),
				linkTo(methodOn(ProposalsController.class).getSections(pid, "")).withRel("sections"));
=======
		//long sid = section.getId();
		//long pid = section.getProposal().getId();
		return new Resource<>(section, Collections.emptyList());
			//	linkTo(methodOn(ProposalsController.class).getSection(pid, sid)).withSelfRel(),
			//	linkTo(methodOn(ProposalsController.class).getSections(pid, "")).withRel("sections"));
>>>>>>> 94ed255f0d232941c3664d260bd9acc1ea411a08
	}

	@Override
	public Resources<Resource<Section>> toResources(Iterable<? extends Section> entities, Proposal proposal) {
		long pid = proposal.getId();
		List<Resource<Section>> sections = 
				StreamSupport.stream(entities.spliterator(), false)
				.map(this::toResource)
				.collect(Collectors.toList());
		return new Resources<>(sections,
				linkTo(methodOn(ProposalsController.class).getSections(pid, "")).withSelfRel(),
				linkTo(methodOn(RootController.class).root()).withRel("root"));
	}

}
