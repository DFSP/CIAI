package pt.unl.fct.ciai.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pt.unl.fct.ciai.model.Section;

public interface SectionsRepository extends CrudRepository<Section, Long> {

    @Query("SELECT s "
            + "FROM Section s "
            + "WHERE s.id LIKE CONCAT('%',:search,'%') "
            + "OR s.goals LIKE CONCAT('%',:search,'%') "
            + "OR s.material LIKE CONCAT('%',:search,'%') "
            + "OR s.workPlan LIKE CONCAT('%',:search,'%') "
            + "OR s.budget LIKE CONCAT('%',:search,'%') "
    )
    Iterable<Section> searchSections(@Param(value = "search") String search);

}
