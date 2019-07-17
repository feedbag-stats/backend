package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.*;
import ch.uzh.feedbag.backend.repository.*;
import ch.uzh.feedbag.backend.service.UserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
public class ResetController {

    private static final int NUMBER_OF_USERS = 2;
    private static final int NUMBER_OF_DAYS = 5;

    private ActivityIntervalRepository ActivityIntervalRepository;
    private UserService userService;
    private UserRepository userRepository;
    private EditLocationRepository editLocationRepository;
    private EventTimeStampRepository eventTimeStampRepository;
    private AllEventsRepository allEventsRepository;

    ResetController(ActivityIntervalRepository activityIntervalRepository, UserService userService, UserRepository userRepository, EditLocationRepository editLocationRepository, EventTimeStampRepository eventTimeStampRepository, AllEventsRepository allEventsRepository) {
        this.ActivityIntervalRepository = activityIntervalRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.editLocationRepository = editLocationRepository;
        this.eventTimeStampRepository = eventTimeStampRepository;
        this.allEventsRepository = allEventsRepository;
    }

    @PostMapping("/reset-database")
    ResponseEntity<String> resetDatabase() {
        this.ActivityIntervalRepository.truncate();
        this.userRepository.truncate();
        this.editLocationRepository.truncate();
        this.eventTimeStampRepository.truncate();
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/generate")
    ResponseEntity<String> generate() {

        List<EditLocation> locations = this.generateFileTree();

        for (int i = 0; i < NUMBER_OF_USERS; i++) {

            // Generate a user
            User user = new User();
            user.setName("User " + i);
            user.setUsername("user" + i);
            User createdUser = this.userService.createUser(user);

            this.generateActivityInterval(createdUser);
            this.generateRandomEvents(createdUser, locations);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    private void generateRandomEvents(User user, List<EditLocation> locations) {
        long duration = 60 * 60 * 24 * NUMBER_OF_DAYS;
        Date today = new Date();
        today.setSeconds(0);
        today.setMinutes(0);
        today.setHours(0);

        long offset = (long) today.getTime() / 1000;
        long spanStart = 0;

        ArrayList<EventTimeStamp> eventTimeStamps = new ArrayList<>();

        while (spanStart <= duration) {
            Duration randomOffset = Duration.ofMillis(getRandomNumberInRange(20, 200));
            String eventType = EventType.getAllTypes()[getRandomNumberInRange(0, EventType.getAllTypes().length)].toString();
            eventTimeStamps.add(new EventTimeStamp(Instant.ofEpochSecond(spanStart + offset), eventType, user));
            spanStart += randomOffset.toMillis();
        }

        this.eventTimeStampRepository.saveAll(eventTimeStamps);


    }

    private void generateActivityInterval(User user) {
        long duration = 60 * 60 * 24 * NUMBER_OF_DAYS;
        Date today = new Date();
        today.setSeconds(0);
        today.setMinutes(0);
        today.setHours(0);

        long offset = (long) today.getTime() / 1000;

        long spanStart = 0;
        ActivityType previousCategory = null;
        int index = 0;

        ArrayList<ActivityInterval> activities = new ArrayList<>();

        while (spanStart <= duration) {
            Duration randomDuration = Duration.ofMillis(getRandomNumberInRange(20, 200));
            ActivityType randomCategory = ActivityType.getAllTypes()[getRandomNumberInRange(0, ActivityType.getAllTypes().length)];
            if (randomCategory == previousCategory) {
                activities.get(index - 1).addDuration(randomDuration);
            } else {
                Instant start = Instant.ofEpochSecond(spanStart + offset);
                Instant end = Instant.ofEpochSecond(spanStart + randomDuration.toMillis() + offset);
                ActivityInterval newActiviy = new ActivityInterval(start, end, randomCategory, user);
                activities.add(newActiviy);
                index++;
            }
            previousCategory = randomCategory;
            spanStart += randomDuration.toMillis();
        }

        this.ActivityIntervalRepository.saveAll(activities);
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min)) + min;
    }

    private List<EditLocation> generateFileTree() {
        List<EditLocation> locations = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader("src/main/resources/filetree.json"));

            JSONObject jsonObject = (JSONObject) obj;

            System.out.println(jsonObject);
            for (Object solution : (JSONArray) jsonObject.get("solutions")) {
                JSONObject solutionObject = (JSONObject) solution;
                String solutionName = (String) solutionObject.get("name");
                for (Object project : (JSONArray) solutionObject.get("projects")) {
                    JSONObject projectObject = (JSONObject) project;
                    String projectName = (String) projectObject.get("name");
                    for (Object package1 : (JSONArray) projectObject.get("packages")) {
                        JSONObject packageObject = (JSONObject) package1;
                        String packageName = (String) packageObject.get("name");
                        for (Object files : (JSONArray) packageObject.get("files")) {
                            String fileName = (String) files;

                            locations.add(new EditLocation(solutionName, projectName, packageName, fileName));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.editLocationRepository.saveAll(locations);

        return locations;
    }

    @GetMapping("/generate_random_events")
    ResponseEntity<String> generateRandomEvents() {
        Date today = new Date();
        today.setSeconds(0);
        today.setMinutes(0);
        today.setHours(0);

        long offset = (long) today.getTime() / 1000;
        long spanStart = 0;

        int NUMBER_OF_EVENTS = 1000;

        Iterable<User> users = this.userRepository.findAll();
        List<AllEvents> events = new ArrayList<>();

        for (User user : users) {
            for (int i = 0; i < NUMBER_OF_EVENTS; i++) {
                Instant instant = Instant.ofEpochSecond(spanStart + offset);
                String eventType = EventType.getAllTypes()[getRandomNumberInRange(0, EventType.getAllTypes().length)].toString();
                AllEvents event = new AllEvents(instant, user, eventType);
                events.add(event);

                Duration randomDuration = Duration.ofMillis(getRandomNumberInRange(20, 200));
                spanStart = spanStart + randomDuration.toMillis();
            }
        }
        this.allEventsRepository.saveAll(events);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
