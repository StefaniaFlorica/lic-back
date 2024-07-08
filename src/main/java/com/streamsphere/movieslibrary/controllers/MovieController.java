package com.streamsphere.movieslibrary.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streamsphere.movieslibrary.entities.Cast;
import com.streamsphere.movieslibrary.entities.Genre;
import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.entities.enums.ItemType;
import com.streamsphere.movieslibrary.models.requests.CastRequest;
import com.streamsphere.movieslibrary.models.requests.MovieRequest;
import com.streamsphere.movieslibrary.models.responses.MovieResponse;
import com.streamsphere.movieslibrary.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.opencsv.CSVReader;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/movie/")
@CrossOrigin(origins = {"https://streamsphere-front.netlify.app"})
public class MovieController {

    @Autowired
    MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieResponse> add(@RequestBody @Valid MovieRequest movieRequest) {
        MovieResponse movieResponse = movieService.add(movieRequest);

        return new ResponseEntity<>(movieResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return new ResponseEntity<>(movieService.getAll(pageable), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<MovieResponse> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(movieService.get(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<MovieResponse> update(@PathVariable Long id, @RequestBody @Valid MovieRequest movieRequest) throws Exception {

        return new ResponseEntity<>(movieService.update(id, movieRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        movieService.delete(id);

        return new ResponseEntity<>("Deleted !", HttpStatus.NO_CONTENT);
    }

    @GetMapping("top-6rated")
    public ResponseEntity<List<MovieResponse>> getTop6RatedMovies() {

        return new ResponseEntity<>(movieService.findTop6ByOrderByRatingDesc(), HttpStatus.OK);
    }

    @GetMapping("top-rated")
    public ResponseEntity<List<MovieResponse>> getTop4RatedMovies() {

        return new ResponseEntity<>(movieService.findTop4ByOrderByRatingDesc(), HttpStatus.OK);
    }

    @GetMapping("latest-by-type")
    public ResponseEntity<List<MovieResponse>> getLatest6MoviesByType(@RequestParam ItemType type) {

        return new ResponseEntity<>(movieService.findTop6ByTypeOrderByReleaseDateDesc(type), HttpStatus.OK);
    }

    @GetMapping("browse")
    public ResponseEntity<List<MovieResponse>> browseMovies(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String genre,
            @RequestParam(defaultValue = "") ItemType type) throws Exception {
        return new ResponseEntity<>(movieService.browseMovies(title, genre, type), HttpStatus.OK);
    }

    @GetMapping("top-reviewed")
    public ResponseEntity<List<MovieResponse>> getTopReviewed() throws Exception {
        return new ResponseEntity<>(movieService.getTop4ReviewedMovies(), HttpStatus.OK);
    }

    @GetMapping("top-fav")
    public ResponseEntity<List<MovieResponse>> getTopFav() throws Exception {
        return new ResponseEntity<>(movieService.getTopFavMovies(), HttpStatus.OK);
    }

    @GetMapping("fav-list/{id}")
    public ResponseEntity<Integer> countMovieInFavList
            (@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(movieService.countMovieInAllMovieLists(id), HttpStatus.OK);
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<?> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] values;
            List<MovieRequest> movieRequests = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            csvReader.readNext(); // Skip header
            while ((values = csvReader.readNext()) != null) {
                if (values.length < 8) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid CSV format");
                }

                MovieRequest movieRequest = new MovieRequest();
                try {
                    String itemTypeStr = values[0].trim();
                    ItemType itemType = ItemType.valueOf(itemTypeStr);
                    movieRequest.setType(itemType);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ItemType value: " + values[0]);
                }

                movieRequest.setTitle(values[1].trim());
                movieRequest.setResume(values[2].trim());
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(values[3].trim());
                movieRequest.setReleaseDate(releaseDate);
                movieRequest.setDirector(values[4].trim());
                movieRequest.setImg(values[5].trim());
                movieRequest.setGenreId(Long.parseLong(values[6].trim())); // Assuming genreId is provided directly

                String castJson = values[7].trim();
                List<Map<String, String>> castList = mapper.readValue(castJson, new TypeReference<List<Map<String, String>>>() {});
                List<CastRequest> castRequests = new ArrayList<>();
                for (Map<String, String> castData : castList) {
                    CastRequest castRequest = new CastRequest();
                    castRequest.setName(castData.get("name").trim());
                    castRequest.setActorName(castData.get("actorName").trim());
                    castRequests.add(castRequest);
                }
                movieRequest.setCasts(castRequests);

                movieRequests.add(movieRequest);
            }
            movieService.saveAll(movieRequests);
            return ResponseEntity.ok("Movies have been uploaded and saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file: " + e.getMessage());
        }
    }
}




