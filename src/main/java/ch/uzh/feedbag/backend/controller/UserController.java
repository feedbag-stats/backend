package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.entity.ZipMapping;
import ch.uzh.feedbag.backend.repository.ZipMappingRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService service;
    private final ZipMappingRepository zipMappingRepository;

    UserController(UserService service, ZipMappingRepository zipMappingRepository) {
        this.service = service;
        this.zipMappingRepository = zipMappingRepository;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    @GetMapping("/me")
    ResponseEntity me(@RequestHeader String Authorization) {
        User user = service.findByToken(Authorization);
        if (null != user) {
            user.setLastUpload(zipMappingRepository.findLatestByUser(user));
            List<ZipMapping> zipMappings = zipMappingRepository.findByMarkedForDeleteUser(user);
            if (zipMappings.size() > 0) {
                user.setRecalculatingStats(true);
            } else {
                user.setRecalculatingStats(false);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.CONFLICT, "No token found!");
    }
}
