package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface LocationIntervalRepository extends CrudRepository<LocationInterval, Long> {
    @Query(value = "SELECT i FROM LocationInterval i " +
            "WHERE i.user = :user " +
            "AND i.begin >= :start " +
            "AND i.end <= :end " +
            "AND i.level = :level " +
            "AND i.location <> '???' " +
            "ORDER BY i.begin ASC")
    List<LocationInterval> findByUserTimespanLevel(@Param("user") User user, @Param("start") Instant start, @Param("end") Instant end, @Param("level") LocationLevel level);
}
