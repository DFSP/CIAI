package pt.unl.fct.ciai.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanReadComment.Condition)
public @interface CanReadComment {
    String Condition = "@SecurityService.isProposalApproved(#pid) or "
    		+ "@SecurityService.isMemberOrStaffOfProposal(principal, #pid) or "
    		+ "@SecurityService.isAdminOfAuthorOfProposal(principal, #pid) or "
    		+ "hasRole(T(pt.unl.fct.ciai.model.User.Role).ROLE_SYS_ADMIN.name())";
}
