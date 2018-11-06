package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanDeleteCompany.Condition)
public @interface CanDeleteCompany {
    String Condition = "(@SecurityService.isSystemAdmin(principal, #id)"
                    + " and @SecurityService.isPrincipal(principal, #id))";
}