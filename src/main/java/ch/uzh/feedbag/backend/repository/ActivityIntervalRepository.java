package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.ActivityInterval;
import ch.uzh.feedbag.backend.entity.AggregatedActivity;
import ch.uzh.feedbag.backend.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ActivityIntervalRepository extends CrudRepository<ActivityInterval, Long> {
	List<ActivityInterval> findByType(String type);


	@Query(value = "SELECT new ch.uzh.feedbag.backend.entity.AggregatedActivity(a.type, cast((SUM(a.end - a.begin)) as integer)) FROM ActivityInterval a WHERE a.user = ?1 GROUP BY a.type")
	List<AggregatedActivity> aggregate(User user);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ActivityInterval e")
	void truncate();

	@Query(value = "SELECT e FROM ActivityInterval e WHERE e.begin > ?1 AND e.end < ?2")
	List<ActivityInterval> findTimespanUser(int start, int end);

	List<ActivityInterval> findByUser(User user);
}
