package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.*;
import ch.uzh.feedbag.backend.repository.ZipMappingRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
public class ZipMappingController {

    private ZipMappingRepository repository;
    private UserService userService;

    ZipMappingController(ZipMappingRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping("/zips")
    ResponseEntity<?> zips(@RequestHeader(name = "Authorization") String token) {
        User user = this.userService.findByToken(token);

        if (null == user) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        List<ZipMapping> zips = repository.findByUser(user);

        return new ResponseEntity<>(zips, HttpStatus.OK);
    }

    @PostMapping("/zip/{id}")
    ResponseEntity<?> toggleZipStatus(@RequestHeader(name = "Authorization") String token, @PathVariable long id) {
        User user = this.userService.findByToken(token);

        if (null == user) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Optional<ZipMapping> optionalZip = repository.findById(id);

        if (optionalZip.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zip not found");
        }

        ZipMapping zipMapping = optionalZip.get();

        if (zipMapping.getUser().getId() != user.getId()) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "This is not allowed");
        }

        zipMapping.setMarkedForDelete(!zipMapping.isMarkedForDelete());
        repository.save(zipMapping);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
