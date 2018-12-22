package pt.unl.fct.ciai.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanDeleteMember.Condition)
public @interface CanDeleteMember {
    String Condition = "@securityService.isAuthorOfProposal(#pid) or "
    		+ "@securityService.isAdminOfAuthorOfProposal(principal, #pid) or "
    		+ "hasRole(T(pt.unl.fct.ciai.model.User.Role).ROLE_SYS_ADMIN.name()) or "
    		+ "@securityService.isPrincipal(principal, #mid)";
}
