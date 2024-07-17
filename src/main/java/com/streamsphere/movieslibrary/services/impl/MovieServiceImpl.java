package com.streamsphere.movieslibrary.services.impl;

import com.streamsphere.movieslibrary.entities.Cast;
import com.streamsphere.movieslibrary.entities.Genre;
import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.entities.enums.ItemType;
import com.streamsphere.movieslibrary.mappers.CastMapper;
import com.streamsphere.movieslibrary.mappers.MovieMapper;
import com.streamsphere.movieslibrary.models.requests.MovieRequest;
import com.streamsphere.movieslibrary.models.responses.MovieResponse;
import com.streamsphere.movieslibrary.repositories.*;
import com.streamsphere.movieslibrary.services.MovieService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    CastRepository castRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MovieListRepository listRepository;

    @Override
    public Map<String, Object> getAll(Pageable pageable) {

        Page<Movie> movieList = movieRepository.findAll(pageable);

        List<MovieResponse> responses = MovieMapper.INSTANCE.mapEntity(movieList.toList());

        Map<String, Object> page = new HashMap<>();
        page.put("content", responses);
        page.put("currentPage", movieList.getNumber());
        page.put("totalElements", movieList.getTotalElements());
        page.put("totalPages", movieList.getTotalPages());

        return page;
    }

    @Override
    public MovieResponse add(MovieRequest movieRequest) {

        Movie movie = MovieMapper.INSTANCE.requestToEntity(movieRequest);

        Genre genre = genreRepository.findById(movieRequest.getGenreId())
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        movie.setGenre(genre);

        movieRepository.save(movie);

        List<Cast> casts = CastMapper.INSTANCE.mapRequests(movieRequest.getCasts());
        if (casts != null) {
            for (Cast cast : casts) {
                cast.setMovie(movie);
                castRepository.save(cast);
            }
            movie.setCastList(casts);
        }

        return MovieMapper.INSTANCE.entityToResponse(movieRepository.save(movie));
    }

    @Override
    public MovieResponse get(Long id) throws Exception {

        Movie movie = movieRepository.findById(id).orElseThrow();

        MovieResponse response = MovieMapper.INSTANCE.entityToResponse(movie);

        response.setNbReviews(reviewRepository.countByMovieId(id));

        return response;
    }

    @Override
    public MovieResponse update(Long id, MovieRequest movieRequest) throws Exception {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new Exception("Movie not found"));

        movie.setImg(movieRequest.getImg());
        movie.setDirector(movieRequest.getDirector());
        movie.setResume(movieRequest.getResume());
        movie.setReleaseDate(movieRequest.getReleaseDate());
        movie.setTitle(movieRequest.getTitle());
        movie.setType(movieRequest.getType());

        Genre genre = genreRepository.findById(movieRequest.getGenreId())
                .orElseThrow(() -> new Exception("Genre not found"));
        movie.setGenre(genre);

        if (movie.getCastList() == null) {
            movie.setCastList(new ArrayList<>());
        } else {
            movie.getCastList().clear();
        }

        List<Cast> casts = CastMapper.INSTANCE.mapRequests(movieRequest.getCasts());
        if (casts != null) {
            for (Cast cast : casts) {
                cast.setMovie(movie);
                movie.getCastList().add(cast);
            }
        }

        return MovieMapper.INSTANCE.entityToResponse(movieRepository.save(movie));
    }

    @Override
    public void delete(Long id) throws Exception {
        Movie movie = movieRepository.findById(id).orElseThrow();

        movieRepository.delete(movie);
    }

    @Override
    public List<MovieResponse> browseMovies(String title, String genreTitle, ItemType type) {

        List<Movie> movies = movieRepository.browseMovies(title, genreTitle, type);

        return MovieMapper.INSTANCE.mapEntity(movies);
    }

    @Override
    public List<MovieResponse> findTop6ByOrderByRatingDesc() {

        List<Movie> movies = movieRepository.findTop6ByOrderByRatingDesc
                (PageRequest.of(0, 6));

        return MovieMapper.INSTANCE.mapEntity(movies);
    }

    @Override
    public List<MovieResponse> findTop4ByOrderByRatingDesc() {
        List<Movie> movies = movieRepository.findTop6ByOrderByRatingDesc
                (PageRequest.of(0, 4));

        return MovieMapper.INSTANCE.mapEntity(movies);
    }

    @Override
    public List<MovieResponse> findTop6ByTypeOrderByReleaseDateDesc(ItemType type) {

        List<Movie> movies = movieRepository.findTop6ByTypeOrderByReleaseDateDesc(type, PageRequest.of(0, 6));

        return MovieMapper.INSTANCE.mapEntity(movies);
    }

    @Override
    public List<MovieResponse> getTop4ReviewedMovies() {
        List<Long> topMovieIds = reviewRepository.findTopReviewedMovies()
                .stream().limit(8).toList();

        return topMovieIds.stream().map(movieId -> {
            Movie movie = movieRepository.findById(movieId).orElseThrow();
            MovieResponse response = new MovieResponse();

            response.setId(movie.getId());
            response.setTitle(movie.getTitle());
            response.setNbReviews(reviewRepository.countByMovieId(movieId));

            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public int countMovieInAllMovieLists(Long movieId) {
        return listRepository.countMovieInAllMovieLists(movieId);
    }

    @Override
    public List<MovieResponse> getTopFavMovies() {
        List<Long> topMovieIds = listRepository.findTopMoviesInLists()
                .stream()
                .limit(6)
                .toList();

        return topMovieIds.stream().map(movieId -> {
            Movie movie = movieRepository.findById(movieId).orElseThrow();

            MovieResponse response = MovieMapper.INSTANCE.entityToResponse(movie);

            response.setNbFav(countMovieInAllMovieLists(movie.getId()));

            return response;
        }).collect(Collectors.toList());
    }
    @Transactional
    @Override
    public void saveAll(List<MovieRequest> movieRequests) {
        for (MovieRequest movieRequest : movieRequests) {
            add(movieRequest);
        }
    }
}
