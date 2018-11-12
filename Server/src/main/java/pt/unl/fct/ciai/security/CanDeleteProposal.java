package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanDeleteProposal.Condition)
public @interface CanDeleteProposal {
    String Condition = "@SecurityService.isAuthorOfProposal(principal,#id) or "
    		+ "@SecurityService.isAdminOfAuthorOfProposal(principal,#id) or "
    		+ "hasRole(T(pt.unl.fct.ciai.model.User.Role).ROLE_SYS_ADMIN.name())";
}