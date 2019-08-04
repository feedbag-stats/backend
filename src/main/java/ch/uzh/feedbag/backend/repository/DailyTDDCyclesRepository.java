package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.ActivityInterval;
import ch.uzh.feedbag.backend.entity.AggregatedActivity;
import ch.uzh.feedbag.backend.entity.DailyTDDCycles;
import ch.uzh.feedbag.backend.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DailyTDDCyclesRepository extends CrudRepository<DailyTDDCycles, Long> {
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM DailyTDDCycles e")
	void truncate();

	@Query(value = "SELECT d FROM DailyTDDCycles d WHERE d.user = ?1 AND d.date >= ?2 AND d.date <= ?3")
	List<DailyTDDCycles> findByTimespanUser(User user, LocalDate start, LocalDate end);

	List<DailyTDDCycles> findByUser(User user);

    @Query(value = "SELECT MAX(d.cycleCount) FROM DailyTDDCycles d WHERE d.user = :user")
    Integer findMaxTDDCyclesByUser(@Param("user") User user);
}
