package pt.unl.fct.ciai.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanModifyComment.Condition)
public @interface CanModifyComment {
    String Condition = "@SecurityService.isAuthorOfComment(principal, #cid)";
}
