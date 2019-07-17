package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.ActivityInterval;
import ch.uzh.feedbag.backend.entity.AggregatedActivity;
import ch.uzh.feedbag.backend.entity.DailyTDDCycles;
import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.repository.ActivityIntervalRepository;
import ch.uzh.feedbag.backend.repository.DailyTDDCyclesRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
public class DailyTDDCyclesController {

    private DailyTDDCyclesRepository repository;
    private UserService userService;

    DailyTDDCyclesController(DailyTDDCyclesRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping("/tdd_cycles")
    ResponseEntity<?> tddCycles(@RequestHeader(name = "Authorization") String token, @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        User user = this.userService.findByToken(token);

        List<DailyTDDCycles> tddCycles = repository.findByTimespanUser(user,startDate,endDate);

        return new ResponseEntity<>(tddCycles, HttpStatus.OK);
    }
}
