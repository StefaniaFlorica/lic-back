package com.streamsphere.movieslibrary.controllers;

import com.streamsphere.movieslibrary.models.requests.ReviewRequest;
import com.streamsphere.movieslibrary.models.responses.MovieResponse;
import com.streamsphere.movieslibrary.models.responses.ReviewResponse;
import com.streamsphere.movieslibrary.services.MovieService;
import com.streamsphere.movieslibrary.services.ReviewService;
import com.streamsphere.movieslibrary.services.impl.OpenAIServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/review/")
@CrossOrigin(origins = { "http://localhost:5173" })
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @Autowired
    private OpenAIServiceImpl openAIServiceImpl;

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<ReviewResponse> add(@RequestBody @Valid ReviewRequest reviewRequest) {
        ReviewResponse reviewResponse = reviewService.add(reviewRequest);

        return new ResponseEntity<>(reviewResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ReviewResponse>> getAll() {

        return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ReviewResponse> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(reviewService.get(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ReviewResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ReviewRequest reviewRequest) throws Exception {

        return new ResponseEntity<>(reviewService.update(id, reviewRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        reviewService.delete(id);

        return new ResponseEntity<>("Deleted !", HttpStatus.NO_CONTENT);
    }

    @GetMapping("movie/{id}")
    public ResponseEntity<List<ReviewResponse>> getByMovie(@PathVariable Long id) throws Exception {

        return new ResponseEntity<>(
                reviewService.findByMovieId(id),
                HttpStatus.OK
        );
    }

    @GetMapping("browse")
    public ResponseEntity<List<ReviewResponse>> getByUserAndMovie(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long movieId) {

        return new ResponseEntity<>(
                reviewService.findByUserAndMovie(userId, movieId),
                HttpStatus.OK
        );
    }

    @GetMapping("/summarize/{movieId}")
    public Mono<String> summarizeReviews(@PathVariable Long movieId) throws Exception {
        List<ReviewResponse> reviews = reviewService.findByMovieId(movieId);
        String reviewTexts = reviews.stream()
                .map(review -> "{\"content\": \"" + review.getContent() + "\", \"rating\": " + review.getRating() + "}")
                .collect(Collectors.joining(", ", "[", "]"));
        MovieResponse movieResponse = movieService.get(movieId);
        String prompt = "The movie title and its reviews are: \"" + movieResponse.getTitle() + "\", " + reviewTexts;
        return openAIServiceImpl.getReviewSummary(prompt);
    }

}
