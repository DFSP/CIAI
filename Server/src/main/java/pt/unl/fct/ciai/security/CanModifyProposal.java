package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanModifyProposal.Condition)
public @interface CanModifyProposal {
    String Condition = "@SecurityService.isAuthorOfProposal(principal,#id)";
    // Not completed yet. I'm working on this...
}