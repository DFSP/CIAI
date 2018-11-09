package pt.unl.fct.ciai.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.User;

public interface UsersRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    @Query("SELECT u "
            + "FROM User u "
            + "WHERE u.id LIKE CONCAT('%',:search,'%') "
            + "OR u.firstName LIKE CONCAT('%',:search,'%') "
            + "OR u.lastName LIKE CONCAT('%',:search,'%') "
            + "OR u.username LIKE CONCAT('%',:search,'%')"
            + "OR u.email LIKE CONCAT('%',:search,'%')"
            + "OR u.role LIKE CONCAT('%',:search,'%')")
    Iterable<User> search(@Param(value = "search") String search);
/*
    @Query("SELECT CASE WHEN u.role = :role THEN TRUE ELSE FALSE END "
            + "FROM User u "
            + "WHERE u.id = :id")
    boolean hasRole(@Param(value = "id") long id, @Param(value = "role") User.Role role);
*/

    // ApproveProposals queries

    @Query("SELECT p " +
            "FROM User u JOIN u.approveProposals p " +
            "WHERE u.id = :id")
    Iterable<Proposal> getApproveProposals(@Param(value = "id") long id);

    @Query("SELECT p "
            + "FROM User u JOIN u.approveProposals p "
            + "WHERE u.id = :uid "
            + "AND "
            + "p.id LIKE CONCAT('%',:search,'%') "
            + "OR p.title LIKE CONCAT('%',:search,'%') "
            + "OR p.description LIKE CONCAT('%',:search,'%') "
            + "OR p.state LIKE CONCAT('%',:search,'%') "
            + "OR p.creationDate LIKE CONCAT('%',:search,'%')"
    )
    Iterable<Proposal> searchApproveProposals(@Param(value = "uid") long uid, @Param(value = "search") String search);

    @Query("SELECT p "
            + "FROM User u JOIN u.approveProposals p "
            + "WHERE u.id = :uid AND p.id = :pid"
    )
    Proposal getApproverInProposal(long uid, long pid);

}
