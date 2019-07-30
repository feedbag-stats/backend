package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

public interface ActivityEntryRepository extends CrudRepository<ActivityEntry, Long> {
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ActivityEntry e")
	void truncate();

	@Query(value = "SELECT d FROM ActivityEntry d WHERE d.user = ?1 AND d.instant >= ?2 AND d.instant <= ?3")
	List<ActivityEntry> findByTimespanUser(User user, Instant start, Instant end);

	List<ActivityEntry> findByUser(User user);

	@Query(value = "SELECT d FROM ActivityEntry d WHERE d.user = :user AND d.instant > :instant ORDER BY d.instant ASC")
	List<ActivityEntry> findNextEventsPaginated(@Param("user") User user, @Param("instant") Instant instant, Pageable pageable);

	default List<ActivityEntry> findNextEvents(User user, Instant instant) {
		return findNextEventsPaginated(user, instant, PageRequest.of(0, 20));
	}

	@Query(value = "SELECT d FROM ActivityEntry d WHERE d.user = :user AND d.instant < :instant ORDER BY d.instant DESC")
	List<ActivityEntry> findPreviousEventsPaginated(@Param("user") User user, @Param("instant") Instant instant, Pageable pageable);

	default List<ActivityEntry> findPreviousEvents(User user, Instant instant) {
		return findPreviousEventsPaginated(user, instant, PageRequest.of(0, 20));
	}

	@Query(value = "SELECT new ch.uzh.feedbag.backend.entity.AggregatedEventsVersion(" +
			"cast(DATE(a.instant) as date), COUNT(a), a.version) " +
			"FROM ActivityEntry a " +
			"WHERE a.instant >= :start " +
			"AND a.instant <= :end " +
			"GROUP BY cast(DATE(a.instant) as date), a.version")
    List<AggregatedEventsVersion> findAggregatedVersionDay(@Param("start") Instant start, @Param("end") Instant end);
}
