package pt.unl.fct.ciai.company;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.unl.fct.ciai.contact.Contact;

public interface CompanyRepository extends CrudRepository<Company, Long> {
	
	@Query("SELECT c "
			+ "FROM Company c "
			+ "WHERE c.id LIKE CONCAT('%',:search,'%') "
			+ "OR c.name LIKE CONCAT('%',:search,'%') "
			+ "OR c.address LIKE CONCAT('%',:search,'%') "
			+ "OR c.email LIKE CONCAT('%',:search,'%')")
	Iterable<Company> searchCompanies(@Param(value = "search") String search);
	
	@Query("SELECT c2 "
			+ "FROM Company c1 JOIN c1.contacts c2 "
			+ "WHERE c2.id LIKE CONCAT('%',:search,'%') "
			+ "OR c2.name LIKE CONCAT('%',:search,'%')")
	Iterable<Contact> searchContacts(@Param(value = "search") String search);

}
