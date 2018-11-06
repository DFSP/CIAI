package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanAddEmployee.Condition)
public @interface CanAddEmployee {
    String Condition = "(@SecurityService.isCompanyAdmin(principal, #cid) "
            + " and @SecurityService.isUserOfSystem(principal, #eid)" //TODO ---> #uid??
            + " and @SecurityService.isPrincipal(principal, #eid))";

}