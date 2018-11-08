package pt.unl.fct.ciai;

import org.springframework.core.annotation.Order;
import org.springframework.hateoas.core.EvoInflectorRelProvider;
import org.springframework.core.Ordered;

import pt.unl.fct.ciai.model.User;

@Deprecated
//@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserRelProvider extends EvoInflectorRelProvider {

	@Override
	public String getCollectionResourceRelFor(final Class<?> type) {
		return super.getCollectionResourceRelFor(User.class);
	}

	@Override
	public String getItemResourceRelFor(final Class<?> type) {
		return super.getItemResourceRelFor(User.class);
	}

	@Override
	public boolean supports(final Class<?> delimiter) {
		return User.class.isAssignableFrom(delimiter);
	}
}
