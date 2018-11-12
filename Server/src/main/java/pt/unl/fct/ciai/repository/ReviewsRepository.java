package pt.unl.fct.ciai.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pt.unl.fct.ciai.model.Review;

public interface ReviewsRepository extends CrudRepository<Review, Long> {

    Review findByTitle(String title);

    @Query("SELECT r "
            + "FROM Review r "
            + "WHERE r.id LIKE CONCAT('%',:search,'%') "
            + "OR r.title LIKE CONCAT('%',:search,'%') "
            + "OR r.text LIKE CONCAT('%',:search,'%') "
            + "OR r.summary LIKE CONCAT('%',:search,'%') "
            + "OR r.classification LIKE CONCAT('%',:search,'%') "
            + "OR r.creationDate LIKE CONCAT('%',:search,'%')"
    )
    Iterable<Review> searchReviews(@Param(value = "search") String search);
}
