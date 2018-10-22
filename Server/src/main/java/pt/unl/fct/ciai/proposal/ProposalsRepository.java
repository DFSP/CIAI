package pt.unl.fct.ciai.proposal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProposalsRepository extends CrudRepository<Proposal, Long> {

    // ? lists documents reviews users employees ?
    @Query("SELECT proposal "
            + "FROM Proposal proposal "
            + "WHERE proposal.id LIKE CONCAT('%',:search,'%') "
            + "OR proposal.isApproved LIKE CONCAT('%',:search,'%') "
            + "OR proposal.date LIKE CONCAT('%',:search,'%') "
            )
    Iterable<Proposal> searchProposals(@Param(value = "search") String search);
}
