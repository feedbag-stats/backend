package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.ActivityEntry;
import ch.uzh.feedbag.backend.entity.AggregatedActivity;
import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.repository.ActivityEntryRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class ActivityEntryController {

    private ActivityEntryRepository repository;
    private UserService userService;

    ActivityEntryController(ActivityEntryRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping("/activity/all")
    ResponseEntity<?> all(@RequestParam int start, @RequestParam int end) {
        System.out.println(start);
        System.out.println(end);

        List<ActivityEntry> acitivites = repository.findTimespanUser(start, end);

        return new ResponseEntity<>(acitivites, HttpStatus.OK);
    }

    @GetMapping("/activity/aggregated")
    ResponseEntity<?> aggregated() {

        List<AggregatedActivity> aggregated = repository.aggregate();
        return new ResponseEntity<>(aggregated, HttpStatus.OK);
    }
}
