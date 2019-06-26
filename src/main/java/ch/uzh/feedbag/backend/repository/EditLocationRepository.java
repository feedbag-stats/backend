package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.EditLocation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("editLocationRepository")
public interface EditLocationRepository extends CrudRepository<EditLocation, Long> {
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM EditLocation e")
	void truncate();
}
