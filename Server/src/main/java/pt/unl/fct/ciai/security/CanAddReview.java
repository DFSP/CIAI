package pt.unl.fct.ciai.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@PreAuthorize(CanAddReview.Condition)
public @interface CanAddReview {
    String Condition = "@SecurityService.isReviewerOfProposal(principal, #id)";
}
