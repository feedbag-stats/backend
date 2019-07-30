package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.*;
import ch.uzh.feedbag.backend.repository.LocationIntervalRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.*;

@RestController
public class LocationStatsController {

    private LocationIntervalRepository repository;
    private UserService userService;

    LocationStatsController(LocationIntervalRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping("/location")
    ResponseEntity<?> location(@RequestHeader(name = "Authorization") String token, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, @RequestParam("location") LocationLevel level) {
        User user = this.userService.findByToken(token);

        Date startDate = (Date) date.clone();
        Date endDate = (Date) date.clone();

        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);

        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setSeconds(59);

        Instant start = startDate.toInstant();
        Instant end = endDate.toInstant();

        List<LocationInterval> locationIntervals = repository.findByUserTimespanLevel(user, start, end, level);

        Map<String, List<LocationInterval>> intervalByName = new HashMap<>();

        locationIntervals.forEach((locationInterval -> {
            String location = locationInterval.getLocation();
            if (!intervalByName.containsKey(location)) {
                intervalByName.put(location, new ArrayList<>());
            }
            intervalByName.get(location).add(locationInterval);
        }));

        return new ResponseEntity<>(intervalByName, HttpStatus.OK);
    }
}
