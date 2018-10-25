package pt.unl.fct.ciai.proposal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProposalsRepository extends CrudRepository<Proposal, Long> {

    @Query("SELECT p "
            + "FROM Proposal p "
            + "WHERE p.id LIKE CONCAT('%',:search,'%') "
            + "OR p.isApproved LIKE CONCAT('%',:search,'%') "
            + "OR p.date LIKE CONCAT('%',:search,'%') "
    )
    Iterable<Proposal> searchProposals(@Param(value = "search") String search);
}
