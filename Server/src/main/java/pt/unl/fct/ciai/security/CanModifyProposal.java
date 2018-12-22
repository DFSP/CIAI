package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanModifyProposal.Condition)
public @interface CanModifyProposal {
    String Condition = "@securityService.isAuthorOfProposal(principal,#id) or "
    		+ "@securityService.isAdminOfAuthorOfProposal(principal,#id)";
}
