package pt.unl.fct.ciai.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pt.unl.fct.ciai.model.Comment;

public interface CommentsRepository extends CrudRepository<Comment, Long> {

    @Query("SELECT c "
            + "FROM Comment c "
            + "WHERE c.id LIKE CONCAT('%',:search,'%') "
            + "OR c.title LIKE CONCAT('%',:search,'%') "
            + "OR c.text LIKE CONCAT('%',:search,'%') "
            + "OR c.date LIKE CONCAT('%',:search,'%') "
    )
    Iterable<Comment> searchComments(@Param(value = "search") String search);
}
