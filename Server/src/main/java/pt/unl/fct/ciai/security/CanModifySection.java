package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanModifySection.Condition)
public @interface CanModifySection {
    String Condition = "@securityService.isAuthorOfProposal(principal,#pid) or "
    		+ "@securityService.isAdminOfAuthorOfProposal(principal,#pid)";
}
