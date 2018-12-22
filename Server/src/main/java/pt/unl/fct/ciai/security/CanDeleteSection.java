package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanDeleteSection.Condition)
public @interface CanDeleteSection {
    String Condition = "@securityService.isAuthorOfProposal(principal,#pid) or "
    		+ "@securityService.isAdminOfAuthorOfProposal(principal,#pid) or "
    		+ "hasRole(T(pt.unl.fct.ciai.model.User.Role).ROLE_SYS_ADMIN.name())";
}
