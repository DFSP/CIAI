package pt.unl.fct.ciai.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pt.unl.fct.ciai.model.Comment;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.Review;
import pt.unl.fct.ciai.model.Section;

public interface ProposalsRepository extends CrudRepository<Proposal, Long> {

    @Query("SELECT p "
            + "FROM Proposal p "
            + "WHERE p.id LIKE CONCAT('%',:search,'%') "
            + "OR p.isApproved LIKE CONCAT('%',:search,'%') "
            + "OR p.date LIKE CONCAT('%',:search,'%') "
    )
    Iterable<Proposal> searchProposals(@Param(value = "search") String search);

    @Query("SELECT s "
    		+ "FROM Proposal p JOIN p.sections s "
    		+ "WHERE p.id = :id") //TODO ver se é mesmo necessario o query explicito
    Iterable<Section> getSectionsByProposalId(@Param(value = "id") long id);
    
    @Query("Select s "
            + "FROM Proposal p JOIN p.sections s "
            + "WHERE p.id = :id "
            + "AND "
            + "s.id LIKE CONCAT('%',:search,'%')"
            + "OR s.goals LIKE CONCAT('%',:search,'%')"
            + "OR s.material LIKE CONCAT('%',:search,'%')"
            + "OR s.workPlan LIKE CONCAT('%',:search,'%')"
            + "OR s.budget LIKE CONCAT('%',:search,'%')"
    )
    Iterable<Section> searchSections(@Param(value = "id") long id, @Param(value = "search") String search);

    @Query("Select r "
            + "FROM Proposal p JOIN p.reviews r "
            + "WHERE r.id LIKE CONCAT('%',:search,'%')"
            + "OR r.title LIKE CONCAT('%',:search,'%')"
            + "OR r.text LIKE CONCAT('%',:search,'%')"
            + "OR r.summary LIKE CONCAT('%',:search,'%')"
            + "OR r.classification LIKE CONCAT('%',:search,'%')"
            + "OR r.date LIKE CONCAT('%',:search,'%')"
    )
    Iterable<Review> searchReviews(@Param(value = "search") String search);

    @Query("Select c "
            + "FROM Proposal p JOIN p.comments c "
            + "WHERE c.id LIKE CONCAT('%',:search,'%')"
            + "OR c.title LIKE CONCAT('%',:search,'%')"
            + "OR c.text LIKE CONCAT('%',:search,'%')"
            + "OR c.date LIKE CONCAT('%',:search,'%')"
    )
    Iterable<Comment> searchComments(@Param(value = "search") String search);

}
