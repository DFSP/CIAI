package pt.unl.fct.ciai.companies;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CompaniesRepository extends CrudRepository<Company, Long> {
	
	@Query("SELECT c "
			+ "FROM Company c "
			+ "WHERE c.id LIKE CONCAT('%',:search,'%') "
			+ "OR c.name LIKE CONCAT('%',:search,'%') "
			+ "OR c.address LIKE CONCAT('%',:search,'%') "
			+ "OR c.email LIKE CONCAT('%',:search,'%')")
	Iterable<Company> search(@Param(value = "search") String search);

}
