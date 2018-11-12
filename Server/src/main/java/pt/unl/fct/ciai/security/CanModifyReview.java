package pt.unl.fct.ciai.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanModifyReview.Condition)
public @interface CanModifyReview {
    String Condition = "@SecurityService.isAuthorOfReview(principal, #rid) or "
    		+ "@SecurityService.isAdminOfAuthorOfReview(principal, #rid)";
}
