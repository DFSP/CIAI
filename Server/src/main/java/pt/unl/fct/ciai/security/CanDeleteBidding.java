package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanDeleteBidding.Condition)
public @interface CanDeleteBidding {
   String Condition = "hasRole(T(pt.unl.fct.ciai.model.User.Role).ROLE_SYS_ADMIN.name()) or "
   		+ "@SecurityService.isPrincipal(principal, #uid) or "
   		+ "@SecurityService.isAdminOfUser(principal, #uid)";
}