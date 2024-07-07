package com.streamsphere.movieslibrary.services;

import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.entities.enums.ItemType;
import com.streamsphere.movieslibrary.models.requests.MovieRequest;
import com.streamsphere.movieslibrary.models.responses.MovieResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface MovieService {

    Map<String, Object> getAll(Pageable pageable);

    MovieResponse add(MovieRequest movieRequest);

    MovieResponse get(Long id) throws Exception;

    MovieResponse update(Long id, MovieRequest movieRequest) throws Exception;

    void delete(Long id) throws Exception;

    List<MovieResponse> browseMovies(String title, String genreTitle, ItemType type);

    List<MovieResponse> findTop6ByOrderByRatingDesc();

    List<MovieResponse> findTop4ByOrderByRatingDesc();

    List<MovieResponse> findTop6ByTypeOrderByReleaseDateDesc(ItemType type);

    List<MovieResponse> getTop4ReviewedMovies();

    int countMovieInAllMovieLists(Long movieId);

    List<MovieResponse> getTopFavMovies();

    @Transactional
    void saveAll(List<MovieRequest> movieRequests);
}
