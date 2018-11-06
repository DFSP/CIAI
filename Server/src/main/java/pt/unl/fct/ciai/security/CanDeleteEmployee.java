package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanDeleteEmployee.Condition)
public @interface CanDeleteEmployee {
    String Condition = "(@SecurityService.isCompanyAdmin(principal, #id) "
            + " and @SecurityService.isSystemAdmin(principal, #id) "
            + " and @SecurityService.isPrincipal(principal, #id))"; //TODO ----> CHECK COM LUIS ----> OR OU AND
}