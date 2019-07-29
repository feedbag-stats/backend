package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.AllEvents;
import ch.uzh.feedbag.backend.entity.DailyTDDCycles;
import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.repository.AllEventsRepository;
import ch.uzh.feedbag.backend.repository.DailyTDDCyclesRepository;
import ch.uzh.feedbag.backend.service.UserService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class AllEventsController {

    private AllEventsRepository repository;
    private UserService userService;

    AllEventsController(AllEventsRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping("/load_events")
    ResponseEntity<?> getNextEvents(@RequestHeader(name = "Authorization") String token, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date date, @RequestParam("direction") String direction) {
        Instant instant = date.toInstant();
        User user = this.userService.findByToken(token);

        List<AllEvents> allEvents;
        if (direction.equals("next")) {
            allEvents = repository.findNextEvents(user, instant);
        } else {
            allEvents = repository.findPreviousEvents(user, instant);
            Collections.reverse(allEvents);
        }

        return new ResponseEntity<>(allEvents, HttpStatus.OK);
    }

    @GetMapping("/event/show")
    ResponseEntity<?> eventShow(@RequestHeader(name = "Authorization") String token, @RequestParam("id") Long id) {

        Optional<AllEvents> event = repository.findById(id);

        if (event.isPresent()) {
            AllEvents allEvents = event.get();

            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            try {
                String json = mapper.writeValueAsString(allEvents);
                return new ResponseEntity<>(json, HttpStatus.OK);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
