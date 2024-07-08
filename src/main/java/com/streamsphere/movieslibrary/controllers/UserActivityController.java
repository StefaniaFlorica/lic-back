package com.streamsphere.movieslibrary.controllers;

import com.streamsphere.movieslibrary.entities.enums.ActivityType;
import com.streamsphere.movieslibrary.models.requests.UserActivityRequest;
import com.streamsphere.movieslibrary.models.responses.UserActivityResponse;
import com.streamsphere.movieslibrary.services.UserActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/user-activity/")
@CrossOrigin(origins = { "https://streamsphere-front.netlify.app" })
public class UserActivityController {

    @Autowired
    UserActivityService userActivityService;

    @PostMapping
    public ResponseEntity<UserActivityResponse> add(@RequestBody @Valid UserActivityRequest userActivityRequest) {
        UserActivityResponse userActivityResponse = userActivityService.add(userActivityRequest);

        return new ResponseEntity<>(userActivityResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<UserActivityResponse>> getAll() {

        return new ResponseEntity<>(userActivityService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserActivityResponse> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(userActivityService.get(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserActivityResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UserActivityRequest userActivityRequest) throws Exception {

        return new ResponseEntity<>(userActivityService.update(id, userActivityRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        userActivityService.delete(id);

        return new ResponseEntity<>("Deleted !", HttpStatus.NO_CONTENT);
    }

    @GetMapping("browse")
    public ResponseEntity<List<UserActivityResponse>> browseActivities(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestParam(required = false) ActivityType type) throws Exception {
        return new ResponseEntity<>(userActivityService.browseActivities(userId, type, date), HttpStatus.OK);
    }

    @GetMapping("/feed/{userId}")
    public List<UserActivityResponse> getUserActivitiesAndFollowing(@PathVariable Long userId) {
        return userActivityService.getUserActivitiesAndFollowing(userId);
    }
}
