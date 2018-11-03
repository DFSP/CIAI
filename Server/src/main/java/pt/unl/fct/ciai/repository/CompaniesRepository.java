package pt.unl.fct.ciai.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;

public interface CompaniesRepository extends JpaRepository<Company, Long> {
	
	@Query("SELECT c "
			+ "FROM Company c "
			+ "WHERE c.id LIKE CONCAT('%',:search,'%') "
			+ "OR c.name LIKE CONCAT('%',:search,'%') "
			+ "OR c.address LIKE CONCAT('%',:search,'%') "
			+ "OR c.email LIKE CONCAT('%',:search,'%')")
	Iterable<Company> searchCompanies(@Param(value = "search") String search);


	@Query("SELECT e "
			+ "FROM Company c JOIN c.employees e "
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
	Iterable<Employee> searchEmployees(@Param(value = "search") String search);

}
