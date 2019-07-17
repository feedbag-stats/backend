package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.AllEvents;
import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.repository.AllEventsRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
public class GlobalStatisticsController {

    private AllEventsRepository repository;
    private UserService userService;

    GlobalStatisticsController(AllEventsRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    /*
    @GetMapping("/statistics_last_seven_days")
    ResponseEntity<?> statisticsLastSevenDays(@RequestHeader(name = "Authorization") String token) {
        User user = this.userService.findByToken(token);



        List<AllEvents> allEvents;
        if (direction.equals("next")) {
            allEvents = repository.findNextEvents(user, instant);
        } else {
            allEvents = repository.findPreviousEvents(user, instant);
            Collections.reverse(allEvents);
        }

        return new ResponseEntity<>(allEvents, HttpStatus.OK);
    }*/
}
