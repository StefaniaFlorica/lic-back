package com.streamsphere.movieslibrary.controllers;

import com.streamsphere.movieslibrary.models.requests.GenreRequest;
import com.streamsphere.movieslibrary.models.responses.GenreResponse;
import com.streamsphere.movieslibrary.services.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genre/")
@CrossOrigin(origins = { "http://localhost:5173" })
public class GenreController {

    @Autowired
    GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreResponse> add(@RequestBody @Valid GenreRequest genreRequest) {
        GenreResponse genreResponse = genreService.add(genreRequest);

        return new ResponseEntity<>(genreResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<GenreResponse>> getAll() {
        return new ResponseEntity<>(genreService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<GenreResponse> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(genreService.get(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<GenreResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid GenreRequest genreRequest) throws Exception {

        return new ResponseEntity<>(genreService.update(id, genreRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        genreService.delete(id);

        return new ResponseEntity<>("Deleted !", HttpStatus.NO_CONTENT);
    }
}
