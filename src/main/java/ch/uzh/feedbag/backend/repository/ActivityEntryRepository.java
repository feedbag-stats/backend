package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.ActivityEntry;
import ch.uzh.feedbag.backend.entity.AggregatedActivity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActivityEntryRepository extends CrudRepository<ActivityEntry, Long> {
	List<ActivityEntry> findByType(String type);

	@Query(value = "SELECT new ch.uzh.feedbag.backend.entity.AggregatedActivity(a.type, SUM(a.end - a.begin)) FROM ActivityEntry a GROUP BY a.type")
	List<AggregatedActivity> aggregate();

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ActivityEntry e")
	void truncate();

	@Query(value = "SELECT e FROM ActivityEntry e WHERE e.begin > ?1 AND e.end < ?2")
	List<ActivityEntry> findTimespanUser(int start, int end);
}
