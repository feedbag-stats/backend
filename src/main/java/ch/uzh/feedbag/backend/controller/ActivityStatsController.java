package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.*;
import ch.uzh.feedbag.backend.repository.ActivityIntervalRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
public class ActivityStatsController {

    private static final int ACTIVITIES_TRESHOLD = 10000;

    private ActivityIntervalRepository repository;
    private UserService userService;

    ActivityStatsController(ActivityIntervalRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping("/activity/all")
    ResponseEntity<?> all(@RequestHeader(name = "Authorization") String token /*@RequestParam int start, @RequestParam int getEnd*/) {
        User user = this.userService.findByToken(token);

/*
        System.out.println(start);
        System.out.println(getEnd);

 */
        List<ActivityInterval> acitivites = repository.findByUser(user);

        return new ResponseEntity<>(acitivites, HttpStatus.OK);
    }


    @GetMapping("/activity/aggregated")
    ResponseEntity<?> aggregated(@RequestHeader(name = "Authorization") String token, @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate, @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        //TODO: Fix Deprication
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);

        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setSeconds(59);

        Instant start = startDate.toInstant();
        Instant end = endDate.toInstant();

        User user = this.userService.findByToken(token);

        List<AggregatedActivity> aggregatedActivities = repository.aggregate(user, start, end);

        // sort by date
        long numberOfDaysBetween = ChronoUnit.DAYS.between(start, end);

        Map<String, Map> days = new HashMap<>();
        List<String> orderedDays = new ArrayList<>();

        for (long i = 0; i <= numberOfDaysBetween; i++) {
            Map<ActivityType, Integer> dayMap = new HashMap<>();

            for (ActivityType activityType : ActivityType.getStatsTypes()) {
                dayMap.put(activityType, 0);
            }

            days.put(startDate.toGMTString(), dayMap);
            orderedDays.add(startDate.toGMTString());

            // add a day
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.DATE, 1); //minus number would decrement the days
            startDate = cal.getTime();
        }

        for (AggregatedActivity aggregate : aggregatedActivities) {
            days.get(aggregate.getDate().toGMTString()).put(aggregate.getType(), aggregate.getDuration());
        }

        // subtract write, debug and testing from active
            for (String day : orderedDays) {
            Integer amountActive = (Integer) days.get(day).get(ActivityType.ACTIVE);
            Integer amountWrite = (Integer) days.get(day).get(ActivityType.WRITE);
            Integer amountDebug = (Integer) days.get(day).get(ActivityType.DEBUG);
            Integer amountTestingstate = (Integer) days.get(day).get(ActivityType.TESTINGSTATE);
            amountActive = Math.max((amountActive - amountWrite - amountDebug - amountTestingstate), 0);
            days.get(day).put(ActivityType.ACTIVE, amountActive);
        }

        List<Map<ActivityType, Integer>> typeDays = new ArrayList<>();
        for (String day : orderedDays) {
            typeDays.add(days.get(day));
        }

        int total = 0;
        Map<ActivityType, Integer> aggregated = new HashMap<>();
        for (ActivityType activityType : ActivityType.getStatsTypes()) {
            aggregated.put(activityType, 0);
        }

        // Aggregate
        for (Map.Entry<String, Map> entry : days.entrySet()) {
            for (ActivityType activityType : ActivityType.getStatsTypes()) {
                Integer value = (Integer) entry.getValue().get(activityType);
                total += value;
                aggregated.put(activityType, value + aggregated.get(activityType));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("days", orderedDays);
        result.put("typeDays", typeDays);
        result.put("aggregated", aggregated);
        result.put("total", total);

        //List<AggregatedActivity> aggregated = new ArrayList<>();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/activity/heatmap")
    ResponseEntity<?> tddCycles(@RequestHeader(name = "Authorization") String token, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        User user = this.userService.findByToken(token);
        int numberOfWeeks = 18;

        //TODO: Fix Deprication
        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setSeconds(59);

        Date startDate = (Date) endDate.clone();
        startDate.setDate(-7 * numberOfWeeks);
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);

        Instant start = startDate.toInstant();
        Instant end = endDate.toInstant();

        List<ActivityHeatmapEntry> heatmapEntries = repository.findHeatmapByUser(user, start, end);
        Integer maxActivities = repository.findMaxActivitiesByUser(user).get(0);

        Map<String, Object> result = new HashMap<>();
        result.put("stats", heatmapEntries);
        result.put("max", Math.min(maxActivities, ACTIVITIES_TRESHOLD));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static long betweenDates(Date firstDate, Date secondDate) {
        return ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());
    }
}
