package com.streamsphere.movieslibrary.service;

import com.streamsphere.movieslibrary.entities.Genre;
import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.entities.enums.ItemType;
import com.streamsphere.movieslibrary.models.requests.MovieRequest;
import com.streamsphere.movieslibrary.models.responses.MovieResponse;
import com.streamsphere.movieslibrary.repositories.*;
import com.streamsphere.movieslibrary.services.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private CastRepository castRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieListRepository listRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie movie;
    private Genre genre;
    private MovieRequest movieRequest;

    @BeforeEach
    public void setUp() {
        genre = new Genre();
        genre.setId(1L);
        genre.setTitle("Action");

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setGenre(genre);
        movie.setType(ItemType.Movie);

        movieRequest = new MovieRequest();
        movieRequest.setTitle("Test Movie");
        movieRequest.setGenreId(1L);
        movieRequest.setType(ItemType.Movie);
        movieRequest.setCasts(Collections.emptyList());
    }

    @Test
    void testGetAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Movie> page = new PageImpl<>(Collections.singletonList(movie));

        when(movieRepository.findAll(pageable)).thenReturn(page);

        Map<String, Object> result = movieService.getAll(pageable);

        assertEquals(1, ((List<MovieResponse>) result.get("content")).size());
        assertEquals(1L, result.get("totalElements"));
        verify(movieRepository, times(1)).findAll(pageable);
    }

    @Test
    void testAdd() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        MovieResponse result = movieService.add(movieRequest);

        assertEquals("Test Movie", result.getTitle());
        verify(movieRepository, times(2)).save(any(Movie.class));
        verify(genreRepository, times(1)).findById(1L);
    }

    @Test
    void testGet() throws Exception {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(reviewRepository.countByMovieId(1L)).thenReturn(5);

        MovieResponse result = movieService.get(1L);

        assertEquals("Test Movie", result.getTitle());
        assertEquals(5, result.getNbReviews());
        verify(movieRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).countByMovieId(1L);
    }

    @Test
    void testUpdate() throws Exception {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        movieRequest.setTitle("Updated Movie");

        MovieResponse result = movieService.update(1L, movieRequest);

        assertEquals("Updated Movie", result.getTitle());
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).save(any(Movie.class));
        verify(genreRepository, times(1)).findById(1L);
    }

    @Test
    void testDelete() throws Exception {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        movieService.delete(1L);

        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).delete(movie);
    }
}
