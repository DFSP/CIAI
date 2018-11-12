package pt.unl.fct.ciai.assembler;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

@Component
public interface SubResourceAssembler<T, E, D extends ResourceSupport> {

    D toResource(T subEntity, E entity);

}
