package pt.unl.fct.ciai.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanAddComment.Condition)
public @interface CanAddComment {
    String Condition = "@securityService.isProposalNotRejected(#id) and "
    		+ "@securityService.isMemberOrStaffOfProposal(principal, #id)";
}
