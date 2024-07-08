package com.streamsphere.movieslibrary.controllers;

import com.streamsphere.movieslibrary.models.requests.CommentRequest;
import com.streamsphere.movieslibrary.models.responses.CommentResponse;
import com.streamsphere.movieslibrary.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment/")
@CrossOrigin(origins = { "https://streamsphere-front.netlify.app" })
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> add(@RequestBody @Valid CommentRequest commentRequest) {
        CommentResponse commentResponse = commentService.add(commentRequest);

        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<CommentResponse>> getAll() {
        return new ResponseEntity<>(commentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentResponse> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(commentService.get(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid CommentRequest commentRequest) throws Exception {

        return new ResponseEntity<>(commentService.update(id, commentRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        commentService.delete(id);

        return new ResponseEntity<>("Deleted !", HttpStatus.NO_CONTENT);
    }

    @GetMapping("review/{id}")
    public ResponseEntity<List<CommentResponse>> getByReview(@PathVariable Long id) {
        return new ResponseEntity<>(commentService.getByReview(id), HttpStatus.OK);
    }
}
