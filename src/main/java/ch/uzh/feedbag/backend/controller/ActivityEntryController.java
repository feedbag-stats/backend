package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.ActivityInterval;
import ch.uzh.feedbag.backend.entity.AggregatedActivity;
import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.repository.ActivityIntervalRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    ResponseEntity<?> aggregated(@RequestHeader(name = "Authorization") String token /*@RequestParam int start, @RequestParam int getEnd*/) {

        User user = this.userService.findByToken(token);

        //List<AggregatedActivity> aggregated = repository.aggregate(user);
        List<AggregatedActivity> aggregated = new ArrayList<>();
        return new ResponseEntity<>(aggregated, HttpStatus.OK);
    }
}
