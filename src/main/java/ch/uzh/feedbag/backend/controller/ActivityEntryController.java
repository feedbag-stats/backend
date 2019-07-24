package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.ActivityInterval;
import ch.uzh.feedbag.backend.entity.ActivityType;
import ch.uzh.feedbag.backend.entity.AggregatedActivity;
import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.repository.ActivityIntervalRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
public class ActivityEntryController {

    private ActivityIntervalRepository repository;
    private UserService userService;

    ActivityEntryController(ActivityIntervalRepository repository, UserService userService) {
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

        Map<String,Map> days = new HashMap<>();

        for (long i = 0; i <= numberOfDaysBetween; i++) {
            Map<ActivityType, Integer> dayMap = new HashMap<>();

            for (ActivityType activityType : ActivityType.getAllTypes()) {
                dayMap.put(activityType, 0);
            }

            days.put(startDate.toGMTString(),dayMap);

            // add a day
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.DATE, 1); //minus number would decrement the days
            startDate = cal.getTime();
        }

        for (AggregatedActivity aggregate: aggregatedActivities) {
            days.get(aggregate.getDate().toGMTString()).put(aggregate.getType(),aggregate.getDuration());
        }

        int total = 0;
        Map<ActivityType,Integer> aggregated = new HashMap<>();
        for (ActivityType activityType : ActivityType.getAllTypes()) {
            aggregated.put(activityType, 0);
        }

        // Aggregate
        for (Map.Entry<String, Map> entry : days.entrySet()) {
            for (ActivityType activityType : ActivityType.getAllTypes()) {
                Integer value = (Integer) entry.getValue().get(activityType);
                total += value;
                aggregated.put(activityType, value + aggregated.get(activityType));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("days",days);
        result.put("aggregated",aggregated);
        result.put("total",total);

        //List<AggregatedActivity> aggregated = new ArrayList<>();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static long betweenDates(Date firstDate, Date secondDate) {
        return ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());
    }
}
