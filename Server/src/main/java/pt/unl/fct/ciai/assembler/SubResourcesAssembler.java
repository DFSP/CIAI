package pt.unl.fct.ciai.assembler;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

public interface SubResourcesAssembler<T, E, D extends ResourceSupport> extends ResourceAssembler<T, Resource<T>> {

    Resources<D> toResources(Iterable<? extends T> entities, E entity);

}
