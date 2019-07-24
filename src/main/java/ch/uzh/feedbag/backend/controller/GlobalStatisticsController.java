package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.AggregatedDailyVariousStats;
import ch.uzh.feedbag.backend.entity.AllEvents;
import ch.uzh.feedbag.backend.entity.DailyVariousStats;
import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.repository.AllEventsRepository;
import ch.uzh.feedbag.backend.repository.DailyVariousStatsRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.data.domain.PageRequest;
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
import java.util.*;

@RestController
public class GlobalStatisticsController {

    private DailyVariousStatsRepository repository;
    private UserService userService;

    GlobalStatisticsController(DailyVariousStatsRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping("/statistics")
    ResponseEntity<?> statistics(@RequestHeader(name = "Authorization") String token, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        User user = this.userService.findByToken(token);

        if (null == user) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Map<Integer, Map> statistics = new HashMap<>();

        int[] intervals = new int[]{7, 30, 90, 360};

        for (int interval : intervals) {
            LocalDate startDate = LocalDate.from(endDate).minusDays(interval);

            AggregatedDailyVariousStats statsUser = this.repository.findByUserTimespanAggregated(user, startDate, endDate);
            AggregatedDailyVariousStats statsGlobal = this.repository.findByTimespanAggregated(startDate, endDate);

            Map<String, AggregatedDailyVariousStats> intervalStats = new HashMap<>();

            intervalStats.put("user", statsUser);
            intervalStats.put("global", statsGlobal);

            statistics.put(interval, intervalStats);
        }

        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
