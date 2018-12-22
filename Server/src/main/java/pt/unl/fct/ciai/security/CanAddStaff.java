package pt.unl.fct.ciai.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanAddStaff.Condition)
public @interface CanAddStaff {
    String Condition = "@securityService.isProposalNotRejected(#id) and "
    		+ "( @SecurityService.isAuthorOfProposal(#id) or "
    		+ "@securityService.isAdminOfAuthorOfProposal(principal, #id) or "
    		+ "hasRole(T(pt.unl.fct.ciai.model.User.Role).ROLE_SYS_ADMIN.name()) )";
}
