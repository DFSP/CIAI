package pt.unl.fct.ciai.review;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReviewsRepository extends CrudRepository<Review, Long> {

    @Query("SELECT review "
            + "FROM Review review "
            + "WHERE review.id LIKE CONCAT('%',:search,'%') "
            + "OR review.title LIKE CONCAT('%',:search,'%') "
            + "OR review.text LIKE CONCAT('%',:search,'%') "
            + "OR review.summary LIKE CONCAT('%',:search,'%') "
            + "OR review.classification LIKE CONCAT('%',:search,'%') "
            //date?
    )
    Iterable<Review> searchReviews(@Param(value = "search") String search);

}
