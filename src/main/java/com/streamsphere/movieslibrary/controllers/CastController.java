package com.streamsphere.movieslibrary.controllers;

import com.streamsphere.movieslibrary.models.requests.CastRequest;
import com.streamsphere.movieslibrary.models.responses.CastResponse;
import com.streamsphere.movieslibrary.services.CastService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cast/")
@CrossOrigin(origins = {"http://localhost:5173"})
public class CastController {

    @Autowired
    CastService castService;

    @PostMapping
    public ResponseEntity<CastResponse> add(@RequestBody @Valid CastRequest castRequest) {
        CastResponse castResponse = castService.add(castRequest);

        return new ResponseEntity<>(castResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<CastResponse>> getAll() {
        return new ResponseEntity<>(castService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<CastResponse> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(castService.get(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<CastResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid CastRequest castRequest) throws Exception {

        return new ResponseEntity<>(castService.update(id, castRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        castService.delete(id);

        return new ResponseEntity<>("Deleted !", HttpStatus.NO_CONTENT);
    }

    @GetMapping("movie/{id}")
    public ResponseEntity<List<CastResponse>> getByMovie(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(castService.getByMovie(id), HttpStatus.OK);
    }
}
