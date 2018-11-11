package pt.unl.fct.ciai.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanReadProposal.Condition)
public @interface CanReadProposal {
    String Condition = "@SecurityService.isProposalApproved(#id) or "
    		+ "@SecurityService.isMemberOrStaffOfProposal(principal, #id) or "
    		+ "@SecurityService.isAdminOfAuthorOfProposal(principal, #id) or "
    		+ "hasRole(T(pt.unl.fct.ciai.model.User.Role).ROLE_SYS_ADMIN.name())";
}
