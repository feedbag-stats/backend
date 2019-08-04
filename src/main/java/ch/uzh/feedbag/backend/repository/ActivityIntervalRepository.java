package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.ActivityHeatmapEntry;
import ch.uzh.feedbag.backend.entity.ActivityInterval;
import ch.uzh.feedbag.backend.entity.AggregatedActivity;
import ch.uzh.feedbag.backend.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public interface ActivityIntervalRepository extends CrudRepository<ActivityInterval, Long> {
	List<ActivityInterval> findByType(String type);


	@Query(value = "SELECT new ch.uzh.feedbag.backend.entity.AggregatedActivity(a.type, cast((SUM(CASE WHEN 1=1 THEN TIMESTAMPDIFF(SECOND,a.begin,a.end) ELSE 0 END)) as integer), cast(DATE(a.begin) as date)) FROM ActivityInterval a WHERE a.user = ?1 AND a.begin > ?2 AND a.end < ?3 GROUP BY cast(DATE(a.begin) as date), a.type")
	List<AggregatedActivity> aggregate(User user, Instant start, Instant end);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ActivityInterval e")
	void truncate();

	@Query(value = "SELECT e FROM ActivityInterval e WHERE e.begin > ?1 AND e.end < ?2")
	List<ActivityInterval> findTimespanUser(int start, int end);

	List<ActivityInterval> findByUser(User user);

	@Query(value = "SELECT new ch.uzh.feedbag.backend.entity.ActivityHeatmapEntry(" +
			"COUNT(a), cast(DATE(a.instant) as date)) " +
			"FROM ActivityEntry a " +
			"WHERE a.user = :user " +
			"AND a.instant >= :start " +
			"AND a.instant <= :end " +
			"GROUP BY cast(DATE(a.instant) as date)")
	List<ActivityHeatmapEntry> findHeatmapByUser(@Param("user") User user, @Param("start") Instant start, @Param("end") Instant end);

	@Query(value = "SELECT COUNT(a) as sum_a FROM ActivityEntry a WHERE a.user = :user GROUP BY cast(DATE(a.instant) as date) ORDER BY sum_a DESC")
    List<Integer> findMaxActivitiesByUser(@Param("user") User user);
}
