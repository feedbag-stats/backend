package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.repository.UserRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

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
    User me(@RequestHeader String Authorization) {
        return service.findByToken(Authorization);
    }

}
