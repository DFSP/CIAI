package pt.unl.fct.ciai.employee;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EmployeesRepository extends CrudRepository<Employee, Long> {

    @Query("SELECT e "
            + "FROM Employee e "
            + "WHERE e.id LIKE CONCAT('%',:search,'%') "
            + "OR e.name LIKE CONCAT('%',:search,'%') "
            + "OR e.address LIKE CONCAT('%',:search,'%') "
            + "OR e.email LIKE CONCAT('%',:search,'%')")
    Iterable<Employee> searchEmployees(@Param(value = "search") String search);

}
