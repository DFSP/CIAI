package pt.unl.fct.ciai.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pt.unl.fct.ciai.company.Company;

public interface UsersRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    @Query("SELECT u "
            + "FROM User u "
            + "WHERE u.id LIKE CONCAT('%',:search,'%') "
            + "OR u.first_name LIKE CONCAT('%',:search,'%') "
            + "OR u.last_name LIKE CONCAT('%',:search,'%') "
            + "OR u.username LIKE CONCAT('%',:search,'%')"
            + "OR u.email LIKE CONCAT('%',:search,'%')")
    Iterable<User> searchUsers(@Param(value = "search") String search);
}
