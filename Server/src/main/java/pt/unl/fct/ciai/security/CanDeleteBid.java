package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanDeleteBid.Condition)
public @interface CanDeleteBid {
   String Condition = "hasRole(T(pt.unl.fct.ciai.model.User.Role).ROLE_SYS_ADMIN.name()) or "
   		+ "@securityService.isPrincipal(principal, #uid) or "
   		+ "@securityService.isAdminOfUser(principal, #uid)";
}
