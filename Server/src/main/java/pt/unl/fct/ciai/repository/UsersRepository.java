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
    Iterable<User> searchUsers(@Param(value = "search") String search);

    @Query("SELECT p " +
            "FROM User u JOIN u.approveProposals p " +
            "WHERE u.id = :id")
    Iterable<Proposal> getApproveProposals(@Param(value = "id") long id);
}
