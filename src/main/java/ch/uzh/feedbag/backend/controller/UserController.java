package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
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
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "No token found!");
    }
}
