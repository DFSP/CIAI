package pt.unl.fct.ciai.company;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.unl.fct.ciai.contact.Contact;
import pt.unl.fct.ciai.employee.Employee;

public interface CompaniesRepository extends CrudRepository<Company, Long> {
	
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
			+ "OR e.name LIKE CONCAT('%',:search,'%')")
	Iterable<Employee> searchEmployees(@Param(value = "search") String search);

}
