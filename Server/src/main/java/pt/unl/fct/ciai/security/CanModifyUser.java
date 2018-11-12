package pt.unl.fct.ciai.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanModifyUser.Condition)
public @interface CanModifyUser {
    String Condition = "hasRole(T(pt.unl.fct.ciai.model.User.Role).ROLE_SYS_ADMIN.name()) or "
    		+ "@SecurityService.isPrincipal(principal, #id) or "
    		+ "@SecurityService.isAdminOfUser(principal, #id)";
}
