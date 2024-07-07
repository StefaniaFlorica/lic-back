package com.streamsphere.movieslibrary.service;

import com.streamsphere.movieslibrary.entities.AppUser;
import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.entities.MovieList;
import com.streamsphere.movieslibrary.models.requests.MovieListRequest;
import com.streamsphere.movieslibrary.models.responses.MovieListResponse;
import com.streamsphere.movieslibrary.repositories.MovieListRepository;
import com.streamsphere.movieslibrary.repositories.MovieRepository;
import com.streamsphere.movieslibrary.repositories.UserActivityRepository;
import com.streamsphere.movieslibrary.repositories.UserRepository;
import com.streamsphere.movieslibrary.services.impl.MovieListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieListServiceTest {

    @Mock
    private MovieListRepository listRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserActivityRepository activityRepository;

    @InjectMocks
    private MovieListServiceImpl listService;

    private MovieList movieList;
    private Movie movie;
    private Movie secondMovie;
    private AppUser user;
    private MovieListRequest movieListRequest;

    @BeforeEach
    public void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");

        secondMovie = new Movie();
        secondMovie.setId(2L);
        secondMovie.setTitle("Another Movie");

        user = new AppUser();
        user.setId(1L);
        user.setUsername("testuser");

        movieList = new MovieList();
        movieList.setId(1L);
        movieList.setName("My List");
        movieList.setUser(user);
        movieList.setMovies(new ArrayList<>(Collections.singletonList(movie)));

        movieListRequest = new MovieListRequest();
        movieListRequest.setName("My List");
        movieListRequest.setUserId(1L);
    }

    @Test
    void testGetAll() {
        when(listRepository.findAll()).thenReturn(Collections.singletonList(movieList));

        List<MovieListResponse> result = listService.getAll();

        assertEquals(1, result.size());
        assertEquals("My List", result.get(0).getName());
        verify(listRepository, times(1)).findAll();
    }

    @Test
    void testAdd() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(listRepository.save(any(MovieList.class))).thenReturn(movieList);

        MovieListResponse result = listService.add(movieListRequest);

        assertEquals("My List", result.getName());
        verify(listRepository, times(1)).save(any(MovieList.class));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGet() throws Exception {
        when(listRepository.findById(1L)).thenReturn(Optional.of(movieList));

        MovieListResponse result = listService.get(1L);

        assertEquals("My List", result.getName());
        verify(listRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdate() throws Exception {
        when(listRepository.findById(1L)).thenReturn(Optional.of(movieList));
        when(listRepository.save(any(MovieList.class))).thenReturn(movieList);

        movieListRequest.setName("Updated List");

        MovieListResponse result = listService.update(1L, movieListRequest);

        assertEquals("Updated List", result.getName());
        verify(listRepository, times(1)).findById(1L);
        verify(listRepository, times(1)).save(any(MovieList.class));
    }

    @Test
    void testDelete() throws Exception {
        when(listRepository.findById(1L)).thenReturn(Optional.of(movieList));

        listService.delete(1L);

        verify(listRepository, times(1)).findById(1L);
        verify(listRepository, times(1)).delete(movieList);
    }

    @Test
    void testAddMovie() throws Exception {
        movieList.setMovies(new ArrayList<>());

        when(listRepository.findById(1L)).thenReturn(Optional.of(movieList));
        lenient().when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.findById(2L)).thenReturn(Optional.of(secondMovie));

        MovieListResponse result = listService.addMovie(1L, 2L);

        assertEquals(1, result.getMovies().size());
        verify(listRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).findById(2L);
        verify(listRepository, times(1)).save(any(MovieList.class));
        verify(activityRepository, times(1)).save(any());
    }

    @Test
    void testDeleteMovie() throws Exception {
        when(listRepository.findById(1L)).thenReturn(Optional.of(movieList));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(listRepository.save(any(MovieList.class))).thenReturn(movieList);

        MovieListResponse result = listService.deleteMovie(1L, 1L);

        assertEquals(0, result.getMovies().size());
        verify(listRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).findById(1L);
        verify(listRepository, times(1)).save(any(MovieList.class));
        verify(activityRepository, times(1)).save(any());
    }

    @Test
    void testGetByUserId() {
        when(listRepository.findByUserId(1L)).thenReturn(Collections.singletonList(movieList));

        List<MovieListResponse> result = listService.getByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("My List", result.get(0).getName());
        verify(listRepository, times(1)).findByUserId(1L);
    }
}