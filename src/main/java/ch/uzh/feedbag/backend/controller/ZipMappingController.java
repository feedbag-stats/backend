package ch.uzh.feedbag.backend.controller;

import ch.uzh.feedbag.backend.entity.*;
import ch.uzh.feedbag.backend.repository.ZipMappingRepository;
import ch.uzh.feedbag.backend.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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

        List<AggregatedZipMapping> zips = repository.findAggregatedByUser(user);

        return new ResponseEntity<>(zips, HttpStatus.OK);
    }

    @PostMapping("/zips/toggle_status")
    ResponseEntity<?> toggleZipStatus(@RequestHeader(name = "Authorization") String token, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        User user = this.userService.findByToken(token);

        if (null == user) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        List<ZipMapping> zipMappings = repository.findByUserDate(user, date);

        boolean isMarkForDelete = false;

        for (ZipMapping zipMapping : zipMappings) {
            isMarkForDelete |= zipMapping.isMarkedForDelete();
        }

        for (ZipMapping zipMapping : zipMappings) {
            if (isMarkForDelete) {
                zipMapping.setMarkedForDelete(false);
            } else {
                zipMapping.setMarkedForDelete(true);
            }
            repository.save(zipMapping);
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
