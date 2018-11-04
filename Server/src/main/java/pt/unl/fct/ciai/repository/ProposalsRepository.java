package pt.unl.fct.ciai.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pt.unl.fct.ciai.model.Proposal;

public interface ProposalsRepository extends CrudRepository<Proposal, Long> {

    @Query("SELECT p "
            + "FROM Proposal p "
            + "WHERE p.id LIKE CONCAT('%',:search,'%') "
            + "OR p.isApproved LIKE CONCAT('%',:search,'%') "
            + "OR p.date LIKE CONCAT('%',:search,'%') "
    )
    Iterable<Proposal> searchProposals(@Param(value = "search") String search);
    
}
