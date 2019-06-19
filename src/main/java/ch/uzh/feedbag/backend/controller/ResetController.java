package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.ActivityInterval;
import ch.uzh.feedbag.backend.entity.ActivityType;
import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.repository.ActivityIntervalRepository;
import ch.uzh.feedbag.backend.repository.UserRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

@RestController
public class ResetController {

    private static final int NUMBER_OF_USERS = 2;

    private ActivityIntervalRepository ActivityIntervalRepository;
    private UserService userService;
    private UserRepository userRepository;

    ResetController(ActivityIntervalRepository activityIntervalRepository, UserService userService, UserRepository userRepository) {
        this.ActivityIntervalRepository = activityIntervalRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/reset-database")
    ResponseEntity<String> resetDatabase() {
        this.ActivityIntervalRepository.truncate();
        this.userRepository.truncate();
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/generate")
    ResponseEntity<String> generate() {

        for (int i = 0; i < NUMBER_OF_USERS; i++) {
            User user = new User();
            user.setName("User " + i);
            user.setUsername("user" + i);
            User createdUser = this.userService.createUser(user);

            int duration = 60 * 60 * 24;
            int offset = 1552435200;

            int spanStart = 0;
            ActivityType previousCategory = null;
            int index = 0;

            ArrayList<ActivityInterval> activities = new ArrayList<>();

            while (spanStart <= duration) {
                if (spanStart < 1552464000 - offset) {
                    spanStart = 1552464000 - offset;
                }
                if (spanStart >= 1552478400 - offset && spanStart < 1552483800 - offset) {
                    spanStart = 1552483800 - offset;
                }
                if (spanStart >= 1552510800 - offset) {
                    break;
                }
                Duration randomDuration = Duration.ofMillis(getRandomNumberInRange(20, 200));
                ActivityType randomCategory = ActivityType.getAllTypes()[getRandomNumberInRange(0, ActivityType.getAllTypes().length)];
                if (randomCategory == previousCategory) {
                    activities.get(index - 1).addDuration(randomDuration);
                } else {
                    Instant start = Instant.ofEpochSecond(spanStart+offset);
                    Instant end = Instant.ofEpochSecond(spanStart + randomDuration.toMillis() + offset);
                    ActivityInterval newActiviy = new ActivityInterval(start,end, randomCategory, createdUser);
                    activities.add(newActiviy);
                    index++;
                }
                previousCategory = randomCategory;
                spanStart += randomDuration.toMillis();
            }

            for (ActivityInterval entry : activities) {
                this.ActivityIntervalRepository.save(entry);
            }
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min)) + min;
    }

}
