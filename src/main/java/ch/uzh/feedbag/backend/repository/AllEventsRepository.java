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
import java.time.LocalDate;
import java.util.List;

public interface AllEventsRepository extends CrudRepository<AllEvents, Long> {
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM AllEvents e")
	void truncate();

	@Query(value = "SELECT d FROM AllEvents d WHERE d.user = ?1 AND d.instant >= ?2 AND d.instant <= ?3")
	List<AllEvents> findByTimespanUser(User user, Instant start, Instant end);

	List<AllEvents> findByUser(User user);

	@Query(value = "SELECT d FROM AllEvents d WHERE d.user = :user AND d.instant > :instant ORDER BY d.instant ASC")
	List<AllEvents> findNextEventsPaginated(@Param("user") User user, @Param("instant") Instant instant, Pageable pageable);

	default List<AllEvents> findNextEvents(User user, Instant instant) {
		return findNextEventsPaginated(user, instant, PageRequest.of(0, 20));
	}

	@Query(value = "SELECT d FROM AllEvents d WHERE d.user = :user AND d.instant < :instant ORDER BY d.instant DESC")
	List<AllEvents> findPreviousEventsPaginated(@Param("user") User user, @Param("instant") Instant instant, Pageable pageable);

	default List<AllEvents> findPreviousEvents(User user, Instant instant) {
		return findPreviousEventsPaginated(user, instant, PageRequest.of(0, 20));
	}

	@Query(value = "SELECT new ch.uzh.feedbag.backend.entity.AggregatedEventsVersion(" +
			"cast(DATE(a.instant) as date), COUNT(a), a.version) " +
			"FROM AllEvents a " +
			"WHERE a.instant >= :start " +
			"AND a.instant <= :end " +
			"GROUP BY cast(DATE(a.instant) as date), a.version")
    List<AggregatedEventsVersion> findAggregatedVersionDay(@Param("start") Instant start, @Param("end") Instant end);
}
