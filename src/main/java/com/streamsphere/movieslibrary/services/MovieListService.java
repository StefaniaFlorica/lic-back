package com.streamsphere.movieslibrary.services;

import com.streamsphere.movieslibrary.models.requests.MovieListRequest;
import com.streamsphere.movieslibrary.models.responses.MovieListResponse;

import java.util.List;

public interface MovieListService {

    List<MovieListResponse> getAll();

    MovieListResponse add(MovieListRequest movieListRequest);

    MovieListResponse get(Long id) throws Exception;

    MovieListResponse update(Long id, MovieListRequest movieListRequest) throws Exception;

    void delete(Long id) throws Exception;

    MovieListResponse addMovie(Long id, Long movieId) throws Exception;

    MovieListResponse deleteMovie(Long id, Long movieId) throws Exception;

    List<MovieListResponse> getByUserId(Long id);

    MovieListResponse findByNameAndUserId(String name, Long userId);
}
