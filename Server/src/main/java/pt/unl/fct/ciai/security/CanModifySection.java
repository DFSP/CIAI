package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanModifySection.Condition)
public @interface CanModifySection {
    String Condition = "@SecurityService.isAuthorOfProposal(principal,#pid) or "
    		+ "@SecurityService.isAdminOfAuthorOfProposal(principal,#pid)";
}