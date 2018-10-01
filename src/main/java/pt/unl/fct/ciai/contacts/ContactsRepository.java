package pt.unl.fct.ciai.contacts;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ContactsRepository extends CrudRepository<Contact, Long> {

	@Query("SELECT c "
			+ "FROM Contact c "
			+ "WHERE c.id LIKE CONCAT('%',:search,'%') "
			+ "OR c.name LIKE CONCAT('%',:search,'%')")
	Iterable<Contact> search(@Param(value = "search") String search);
	
}
