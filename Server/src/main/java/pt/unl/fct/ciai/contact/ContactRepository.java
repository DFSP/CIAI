package pt.unl.fct.ciai.contact;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends CrudRepository<Contact, Long> {

    Contact findByName(String name);
	
	@Query("SELECT c "
			+ "FROM Contact c "
			+ "WHERE c.id LIKE CONCAT('%',:search,'%') "
			+ "OR c.name LIKE CONCAT('%',:search,'%')")
	Iterable<Contact> search(@Param(value = "search") String search);
	
}
