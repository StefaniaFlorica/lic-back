package com.streamsphere.movieslibrary.services.impl;

import com.streamsphere.movieslibrary.entities.AppUser;
import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.entities.MovieList;
import com.streamsphere.movieslibrary.entities.UserActivity;
import com.streamsphere.movieslibrary.entities.enums.ActivityType;
import com.streamsphere.movieslibrary.mappers.MovieListMapper;
import com.streamsphere.movieslibrary.models.requests.MovieListRequest;
import com.streamsphere.movieslibrary.models.responses.MovieListResponse;
import com.streamsphere.movieslibrary.repositories.MovieListRepository;
import com.streamsphere.movieslibrary.repositories.MovieRepository;
import com.streamsphere.movieslibrary.repositories.UserActivityRepository;
import com.streamsphere.movieslibrary.repositories.UserRepository;
import com.streamsphere.movieslibrary.services.MovieListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MovieListServiceImpl implements MovieListService {

    @Autowired
    MovieListRepository listRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserActivityRepository activityRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<MovieListResponse> getAll() {
        List<MovieList> lists = listRepository.findAll();

        return MovieListMapper.INSTANCE.mapEntity(lists);
    }

    @Override
    public MovieListResponse add(MovieListRequest movieListRequest) {

        MovieList list = MovieListMapper.INSTANCE.requestToEntity(movieListRequest);

        AppUser user = userRepository.findById(movieListRequest.getUserId()).orElseThrow();

        list.setUser(user);

        return MovieListMapper.INSTANCE.entityToResponse(listRepository.save(list));
    }

    @Override
    public MovieListResponse get(Long id) throws Exception {

        MovieList list = listRepository.findById(id).orElseThrow();

        return MovieListMapper.INSTANCE.entityToResponse(list);
    }

    @Override
    public MovieListResponse update(Long id, MovieListRequest movieListRequest) throws Exception {

        MovieList list = listRepository.findById(id).orElseThrow();

        list.setName(movieListRequest.getName());

//        list.getMovies().clear();
//
//        for (Long movieId : movieListRequest.getMoviesId()) {
//            Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new Exception("Movie not found"));
//            list.getMovies().add(movie);
//        }

        return MovieListMapper.INSTANCE.entityToResponse(listRepository.save(list));
    }

    @Override
    public void delete(Long id) throws Exception {
        MovieList list = listRepository.findById(id).orElseThrow();

        listRepository.delete(list);
    }

    @Override
    public MovieListResponse addMovie(Long id, Long movieId) throws Exception {

        MovieList list = listRepository.findById(id).orElseThrow(() -> new Exception("MovieList not found"));
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new Exception("Movie not found"));

        if (!list.getMovies().contains(movie)) {
            List<Movie> movies = new ArrayList<>(list.getMovies());
            movies.add(movie);
            list.setMovies(movies);
            listRepository.save(list);

            UserActivity activity = UserActivity.builder()
                    .date(new Date())
                    .type(ActivityType.Favorite)
                    .action("Add")
                    .user(list.getUser())
                    .movieTitle(movie.getTitle())
                    .movieId(movie.getId())
                    .reviewId(0L)
                    .build();

            activityRepository.save(activity);
        }

        return MovieListMapper.INSTANCE.entityToResponse(list);
    }

    @Override
    public MovieListResponse deleteMovie(Long id, Long movieId) throws Exception {

        MovieList list = listRepository.findById(id).orElseThrow(() -> new Exception("MovieList not found"));
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new Exception("Movie not found"));

        if (list.getMovies().contains(movie)) {
            list.getMovies().remove(movie);
            List<Movie> movies = new ArrayList<>(list.getMovies());
            movies.remove(movie);
            list.setMovies(movies);
            listRepository.save(list);

            UserActivity activity = UserActivity.builder()
                    .date(new Date())
                    .type(ActivityType.Favorite)
                    .action("Delete")
                    .user(list.getUser())
                    .movieTitle(movie.getTitle())
                    .build();

            activityRepository.save(activity);
        }

        return MovieListMapper.INSTANCE.entityToResponse(list);
    }

    @Override
    public List<MovieListResponse> getByUserId(Long id) {

        List<MovieList> lists = listRepository.findByUserId(id);

        return MovieListMapper.INSTANCE.mapEntity(lists);
    }

    @Override
    public MovieListResponse findByNameAndUserId(String name, Long userId) {
        return listRepository.findByNameAndUserId(name, userId)
                .map(MovieListMapper.INSTANCE::entityToResponse)
                .orElse(null);
    }
}
