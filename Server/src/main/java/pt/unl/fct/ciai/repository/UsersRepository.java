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

    @Query("SELECT p "
            + "FROM User u JOIN u.proposals p "
            + "WHERE u.id = :uid"
    )
    Iterable<Proposal> getProposals(@Param(value = "uid") long uid);
/*
    @Query("SELECT CASE WHEN u.role = :role THEN TRUE ELSE FALSE END "
            + "FROM User u "
            + "WHERE u.id = :id")
    boolean hasRole(@Param(value = "id") long id, @Param(value = "role") User.Role role);
*/

}
