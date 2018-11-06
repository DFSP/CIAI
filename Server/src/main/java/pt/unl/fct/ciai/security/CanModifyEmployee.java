package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanModifyEmployee.Condition)
public @interface CanModifyEmployee {
    String Condition = "(@SecurityService.isCompanyAdmin(principal, #cid) "
            + " and @SecurityService.isMemberOfMyCompany(#cid, #eid)"
    		+ " and @SecurityService.isPrincipal(principal, #eid))";
}