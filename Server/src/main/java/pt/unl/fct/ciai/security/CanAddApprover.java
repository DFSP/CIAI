package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanAddApprover.Condition)
public @interface CanAddApprover {
   String Condition = "@SecurityService.isAdminOfUser(principal, #id)";
}