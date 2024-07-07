package com.streamsphere.movieslibrary.controllers;

import com.streamsphere.movieslibrary.models.requests.MovieListRequest;
import com.streamsphere.movieslibrary.models.responses.MovieListResponse;
import com.streamsphere.movieslibrary.services.MovieListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie-list/")
@CrossOrigin(origins = { "http://localhost:5173" })
public class MovieListController {

    @Autowired
    MovieListService movieListService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid MovieListRequest movieListRequest) {
        if (movieListService.findByNameAndUserId(movieListRequest.getName(), movieListRequest.getUserId()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: List already exists!");
        }
        MovieListResponse movieListResponse = movieListService.add(movieListRequest);

        return new ResponseEntity<>(movieListResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<MovieListResponse>> getAll() {

        return new ResponseEntity<>(movieListService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<MovieListResponse> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(movieListService.get(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<MovieListResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid MovieListRequest movieListRequest) throws Exception {

        return new ResponseEntity<>(movieListService.update(id, movieListRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        movieListService.delete(id);

        return new ResponseEntity<>("Deleted !", HttpStatus.NO_CONTENT);
    }

    @GetMapping("{id}/add/{movieId}")
    public ResponseEntity<MovieListResponse> addMovie
            (@PathVariable Long id, @PathVariable Long movieId) throws Exception {

        return new ResponseEntity<>(movieListService.addMovie(id, movieId), HttpStatus.OK);
    }

    @GetMapping("{id}/delete/{movieId}")
    public ResponseEntity<MovieListResponse> deleteMovie
            (@PathVariable Long id, @PathVariable Long movieId) throws Exception {

        return new ResponseEntity<>(movieListService.deleteMovie(id, movieId), HttpStatus.OK);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<List<MovieListResponse>> getByUserId
            (@PathVariable Long id) throws Exception {

        return new ResponseEntity<>(movieListService.getByUserId(id), HttpStatus.OK);
    }
}
