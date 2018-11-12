package pt.unl.fct.ciai.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;

public interface CompaniesRepository extends CrudRepository<Company, Long> {
	
	@Query("SELECT c "
			+ "FROM Company c "
			+ "WHERE c.id LIKE CONCAT('%',:search,'%') "
			+ "OR c.name LIKE CONCAT('%',:search,'%') "
			+ "OR c.address LIKE CONCAT('%',:search,'%') "
			+ "OR c.email LIKE CONCAT('%',:search,'%')")
	Iterable<Company> searchCompanies(@Param(value = "search") String search);


	// Employees queries

	@Query("SELECT e "
			+ "FROM Company c JOIN c.employees e "
			+ "WHERE c.id = :cid"
	)
	Iterable<Employee> getEmployees(@Param(value = "cid") long cid);

	@Query("SELECT e "
			+ "FROM Company c JOIN c.employees e "
			+ "WHERE c.id = :cid "
			+ "AND "
			+ "(e.id LIKE CONCAT('%',:search,'%') "
			+ "OR e.city LIKE CONCAT('%',:search,'%')"
			+ "OR e.address LIKE CONCAT('%',:search,'%')"
			+ "OR e.zipCode LIKE CONCAT('%',:search,'%')"
			+ "OR e.cellPhone LIKE CONCAT('%',:search,'%')"
			+ "OR e.homePhone LIKE CONCAT('%',:search,'%')"
			+ "OR e.gender LIKE CONCAT('%',:search,'%')"
			+ "OR e.salary LIKE CONCAT('%',:search,'%')"
			+ "OR e.birthday LIKE CONCAT('%',:search,'%'))"
	)
	Iterable<Employee> searchEmployees(@Param(value = "cid") long cid, @Param(value = "search") String search);

	@Query("SELECT e "
			+ "FROM Company c JOIN c.employees e "
			+ "WHERE c.id = :cid AND e.id = :eid"
	)
	Employee getEmployee(@Param(value = "cid") long cid, @Param(value = "eid") long eid);

	@Query("SELECT CASE WHEN e IS NOT NULL THEN TRUE ELSE FALSE END " +
			"FROM Company c JOIN c.employees e " +
			"WHERE c.id = :cid AND e.id = :eid")
	boolean existsEmployee(@Param(value = "cid") long cid, @Param(value = "eid") long eid);

}
