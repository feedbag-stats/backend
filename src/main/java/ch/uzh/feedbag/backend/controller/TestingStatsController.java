package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.*;
import ch.uzh.feedbag.backend.repository.DailyTDDCyclesRepository;
import ch.uzh.feedbag.backend.repository.DailyVariousStatsRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TestingStatsController {

    private DailyTDDCyclesRepository tddCyclesRepository;
    private DailyVariousStatsRepository dailyVariousStatsRepository;
    private UserService userService;

    TestingStatsController(DailyTDDCyclesRepository tddCyclesRepository, DailyVariousStatsRepository dailyVariousStatsRepository, UserService userService) {
        this.tddCyclesRepository = tddCyclesRepository;
        this.dailyVariousStatsRepository = dailyVariousStatsRepository;
        this.userService = userService;
    }

    @GetMapping("/testing/tdd_cycles")
    ResponseEntity<?> tddCycles(@RequestHeader(name = "Authorization") String token, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        User user = this.userService.findByToken(token);

        int numberOfWeeks = 18;

        LocalDate startDate = LocalDate.from(date).minusDays(7 * numberOfWeeks);
        LocalDate endDate = date;

        List<DailyTDDCycles> tddCycles = tddCyclesRepository.findByTimespanUser(user,startDate,endDate);

        return new ResponseEntity<>(tddCycles, HttpStatus.OK);
    }

    @GetMapping("/testing/testruns")
    ResponseEntity<?> testRuns(@RequestHeader(name = "Authorization") String token, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        User user = this.userService.findByToken(token);

        int numberOfWeeks = 18;

        LocalDate startDate = LocalDate.from(date).minusDays(7 * numberOfWeeks);
        LocalDate endDate = date;

        List<DailyVariousStats> dailyVariousStats = dailyVariousStatsRepository.findByUserTimespan(user,startDate,endDate);

        return new ResponseEntity<>(dailyVariousStats, HttpStatus.OK);
    }
}
