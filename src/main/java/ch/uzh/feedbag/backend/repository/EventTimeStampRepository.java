package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.EventTimeStamp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("eventTimeStampRepository")
public interface EventTimeStampRepository extends CrudRepository<EventTimeStamp, Long> {
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM EventTimeStamp e")
	void truncate();
}
