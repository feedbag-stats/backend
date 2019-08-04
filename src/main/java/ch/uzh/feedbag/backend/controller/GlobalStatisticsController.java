package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.*;
import ch.uzh.feedbag.backend.repository.ActivityEntryRepository;
import ch.uzh.feedbag.backend.repository.DailyVariousStatsRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
public class GlobalStatisticsController {

    private DailyVariousStatsRepository dailyVariousStatsRepository;
    private ActivityEntryRepository activityEntryRepository;
    private UserService userService;

    GlobalStatisticsController(DailyVariousStatsRepository dailyVariousStatsRepository, ActivityEntryRepository activityEntryRepository, UserService userService) {
        this.dailyVariousStatsRepository = dailyVariousStatsRepository;
        this.activityEntryRepository = activityEntryRepository;
        this.userService = userService;
    }

    @GetMapping("/statistics/table")
    ResponseEntity<?> statisticsTable(@RequestHeader(name = "Authorization") String token, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        User user = this.userService.findByToken(token);

        if (null == user) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Map<Integer, Map> statistics = new HashMap<>();

        int[] intervals = new int[]{7, 30, 90, 360};

        for (int interval : intervals) {
            LocalDate startDate = LocalDate.from(endDate).minusDays(interval);

            AggregatedDailyVariousStats statsUser = this.dailyVariousStatsRepository.findByUserTimespanAggregated(user, startDate, endDate);
            if (statsUser == null) {
                statsUser = new AggregatedDailyVariousStats(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            }

            AggregatedDailyVariousStats statsGlobal = this.dailyVariousStatsRepository.findByTimespanAggregated(startDate, endDate);

            Map<String, AggregatedDailyVariousStats> intervalStats = new HashMap<>();

            intervalStats.put("user", statsUser);
            intervalStats.put("global", statsGlobal);

            statistics.put(interval, intervalStats);
        }

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/statistics/activities")
    ResponseEntity<?> statisticsActivities(@RequestHeader(name = "Authorization") String token, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        User user = this.userService.findByToken(token);

        if (null == user) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        int numberOfWeeks = 24;

        //TODO: Fix Deprication
        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setSeconds(59);

        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.DATE, -7 * numberOfWeeks + 1);
        Date startDate = cal.getTime();
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);

        Instant start = startDate.toInstant();
        Instant end = endDate.toInstant();

        Map<Integer, Map> statistics = new HashMap<>();

        List<AggregatedEventsVersion> aggregatedEventsVersions = activityEntryRepository.findAggregatedVersionDay(start, end);

        // sort by date
        long numberOfDaysBetween = ChronoUnit.DAYS.between(start, end);

        List<String> days = new ArrayList<>();

        for (long i = 0; i <= numberOfDaysBetween; i++) {
            days.add(startDate.toGMTString());

            // add a day
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(startDate);
            cal2.add(Calendar.DATE, 1); //minus number would decrement the days
            startDate = cal2.getTime();
        }

        Map<String, List<Long>> activities = new HashMap<>();

        Set<String> versions = new HashSet<>();
        aggregatedEventsVersions.forEach((aggregatedEventsVersion) -> {
            versions.add(aggregatedEventsVersion.getVersion());
        });
        versions.forEach((version) -> {
            Map<String, Long> dayVersion = new HashMap<>();
            List<Long> data = new ArrayList<>();
            aggregatedEventsVersions.forEach((aggregatedEventsVersion) -> {
                if (aggregatedEventsVersion.getVersion().equals(version)) {
                    dayVersion.put(aggregatedEventsVersion.getDate().toGMTString(), aggregatedEventsVersion.getCount());
                }
            });
            days.forEach((day) -> {
                if (dayVersion.containsKey(day)) {
                    data.add(dayVersion.get(day));
                } else {
                    data.add((long) 0);
                }
                dayVersion.put(day, (long) 0);
            });
            activities.put(version, data);
        });

        Map<String, Object> result = new HashMap<>();
        result.put("days", days);
        result.put("activities", activities);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
