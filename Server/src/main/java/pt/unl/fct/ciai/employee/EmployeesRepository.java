package pt.unl.fct.ciai.employee;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EmployeesRepository extends CrudRepository<Employee, Long> {

    @Query("SELECT e "
            + "FROM Employee e "
            + "WHERE e.id LIKE CONCAT('%',:search,'%') "
            + "OR e.city LIKE CONCAT('%',:search,'%')"
            + "OR e.address LIKE CONCAT('%',:search,'%')"
            + "OR e.zipCode LIKE CONCAT('%',:search,'%')"
            + "OR e.cellPhone LIKE CONCAT('%',:search,'%')"
            + "OR e.homePhone LIKE CONCAT('%',:search,'%')"
            + "OR e.gender LIKE CONCAT('%',:search,'%')"
            + "OR e.salary LIKE CONCAT('%',:search,'%')"
            + "OR e.birthday LIKE CONCAT('%',:search,'%')"
    )
    Iterable<Employee> searchEmployee(@Param(value = "search") String search);


}
