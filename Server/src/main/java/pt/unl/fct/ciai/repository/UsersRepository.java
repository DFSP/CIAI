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
            + "OR u.username LIKE CONCAT('%',:search,'%') "
            + "OR u.email LIKE CONCAT('%',:search,'%') "
            + "OR u.role LIKE CONCAT('%',:search,'%')")
    Iterable<User> searchUsers(@Param(value = "search") String search);

    // Proposals

    @Query("SELECT p "
            + "FROM User u JOIN u.proposals p "
            + "WHERE u.id = :uid"
    )
    Iterable<Proposal> getProposals(@Param(value = "uid") long uid);

    @Query("SELECT p "
            + "FROM User u JOIN u.proposals p "
            + "WHERE u.id = :uid "
            + "AND "
            + "(p.id LIKE CONCAT('%',:search,'%') "
            + "OR p.title LIKE CONCAT('%',:search,'%') "
            + "OR p.description LIKE CONCAT('%',:search,'%') "
            + "OR p.state LIKE CONCAT('%',:search,'%') "
            + "OR p.creationDate LIKE CONCAT('%',:search,'%'))"
    )
    Iterable<Proposal> searchProposals(@Param(value = "uid") long uid, @Param(value = "search") String search);

    @Query("SELECT p "
            + "FROM User u JOIN u.proposals p "
            + "WHERE u.id = :uid AND p.id = :pid"
    )
    Proposal getProposal(@Param(value = "uid") long uid, @Param(value = "pid") long pid);

    // Biddings

    @Query("SELECT b "
            + "FROM User u JOIN u.biddings b "
            + "WHERE u.id = :uid"
    )
    Iterable<Proposal> getBiddings(@Param(value = "uid") long uid);

    @Query("SELECT p "
            + "FROM User u JOIN u.proposals p "
            + "WHERE u.id = :uid "
            + "AND "
            + "(p.id LIKE CONCAT('%',:search,'%') "
            + "OR p.title LIKE CONCAT('%',:search,'%') "
            + "OR p.description LIKE CONCAT('%',:search,'%') "
            + "OR p.state LIKE CONCAT('%',:search,'%') "
            + "OR p.creationDate LIKE CONCAT('%',:search,'%'))"
    )
    Iterable<Proposal> searchBiddings(@Param(value = "uid") long uid, @Param(value = "search") String search);

    @Query("SELECT b "
            + "FROM User u JOIN u.biddings b "
            + "WHERE u.id = :uid AND b.id = :pid"
    )
    Proposal getBidding(@Param(value = "uid") long uid, @Param(value = "pid") long pid);

}
