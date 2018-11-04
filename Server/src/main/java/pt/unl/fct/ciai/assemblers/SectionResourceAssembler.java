package pt.unl.fct.ciai.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.model.Section;

@Component
public class SectionResourceAssembler implements ResourceAssembler<Section, Resource<Section>> {

	@Override
	public Resource<Section> toResource(Section section) {
		long sid = section.getId();
		long pid = section.getProposal().getId();
		return new Resource<>(section,
				linkTo(methodOn(ProposalsController.class).getSection(pid, sid)).withSelfRel(),
				linkTo(methodOn(ProposalsController.class).getSections(pid)).withRel("sections"));
	}

}
