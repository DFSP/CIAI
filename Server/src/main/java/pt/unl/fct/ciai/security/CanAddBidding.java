package pt.unl.fct.ciai.security;

import java.lang.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanAddBidding.Condition)
public @interface CanAddBidding {
   String Condition = "#user.username == principal.username";
}