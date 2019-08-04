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

public interface DailyVariousStatsRepository extends CrudRepository<DailyVariousStats, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM DailyVariousStats e")
    void truncate();

    @Query(value = "SELECT d FROM DailyVariousStats d WHERE d.user = :user AND d.date > :startDate AND d.date <= :endDate ORDER BY d.date DESC")
    List<DailyVariousStats> findByUserTimespan(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT " +
            "new ch.uzh.feedbag.backend.entity.AggregatedDailyVariousStats(" +
            "coalesce(SUM(d.totalBuildDurationInMs)/SUM(d.buildCount),0)," +
            "coalesce(AVG(d.solutionSwitches),0)," +
            "coalesce(AVG(d.packageSwitches),0)," +
            "coalesce(AVG(d.buildCount),0)," +
            "coalesce(AVG(d.testsFixed),0)," +
            "coalesce(AVG(d.testsRun),0)," +
            "coalesce(AVG(d.successfulTests),0)," +
            "coalesce(AVG(d.commits),0)," +
            "coalesce(AVG(d.numSessions),0)," +
            "coalesce(AVG(d.numSessionsLongerThanTenMin),0)," +
            "coalesce(AVG(d.totalSessionDurationMillis),0)," +
            "coalesce(SUM(d.totalSessionDurationMillis)/SUM(d.numSessions),0)," +
            "coalesce(AVG(d.breaks),0)," +
            "coalesce(AVG(d.filesEdited),0)" +
            ")" +
            "FROM DailyVariousStats d " +
            "WHERE d.user = :user " +
            "AND d.date > :startDate " +
            "AND d.date <= :endDate " +
            "GROUP BY d.user")
    AggregatedDailyVariousStats findByUserTimespanAggregated(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT " +
            "new ch.uzh.feedbag.backend.entity.AggregatedDailyVariousStats(" +
            "coalesce(SUM(d.totalBuildDurationInMs)/SUM(d.buildCount),0)," +
            "coalesce(AVG(d.solutionSwitches),0)," +
            "coalesce(AVG(d.packageSwitches),0)," +
            "coalesce(AVG(d.buildCount),0)," +
            "coalesce(AVG(d.testsFixed),0)," +
            "coalesce(AVG(d.testsRun),0)," +
            "coalesce(AVG(d.successfulTests),0)," +
            "coalesce(AVG(d.commits),0)," +
            "coalesce(AVG(d.numSessions),0)," +
            "coalesce(AVG(d.numSessionsLongerThanTenMin),0)," +
            "coalesce(AVG(d.totalSessionDurationMillis),0)," +
            "coalesce(SUM(d.totalSessionDurationMillis)/SUM(d.numSessions),0)," +
            "coalesce(AVG(d.breaks),0)," +
            "coalesce(AVG(d.filesEdited),0)" +
            ")" +
            "FROM DailyVariousStats d " +
            "WHERE d.date > :startDate " +
            "AND d.date <= :endDate")
    AggregatedDailyVariousStats findByTimespanAggregated(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT MAX(d.testsRun) FROM DailyVariousStats d WHERE d.user = :user")
    Integer findMaxTestRunsByUser(@Param("user") User user);
}
