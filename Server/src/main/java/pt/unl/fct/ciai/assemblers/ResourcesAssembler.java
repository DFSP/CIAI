package pt.unl.fct.ciai.assemblers;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;

public interface ResourcesAssembler<T, D extends ResourceSupport> extends ResourceAssembler<T, Resource<T>> {

	Resources<D> toResources(Iterable<? extends T> entities);

}
