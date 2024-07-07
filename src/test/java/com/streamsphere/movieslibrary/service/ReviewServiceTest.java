package com.streamsphere.movieslibrary.service;
import com.streamsphere.movieslibrary.entities.AppUser;
import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.entities.Review;
import com.streamsphere.movieslibrary.models.requests.ReviewRequest;
import com.streamsphere.movieslibrary.models.responses.ReviewResponse;
import com.streamsphere.movieslibrary.repositories.MovieRepository;
import com.streamsphere.movieslibrary.repositories.ReviewRepository;
import com.streamsphere.movieslibrary.repositories.UserActivityRepository;
import com.streamsphere.movieslibrary.repositories.UserRepository;
import com.streamsphere.movieslibrary.services.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserActivityRepository activityRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Review review;
    private Movie movie;
    private AppUser user;
    private ReviewRequest reviewRequest;

    @BeforeEach
    public void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");

        user = new AppUser();
        user.setId(1L);
        user.setUsername("testuser");

        review = new Review();
        review.setId(1L);
        review.setContent("Great movie!");
        review.setRating(5);
        review.setDate(new Date());
        review.setMovie(movie);
        review.setUser(user);

        reviewRequest = new ReviewRequest();
        reviewRequest.setContent("Great movie!");
        reviewRequest.setRating(5);
        reviewRequest.setMovieId(1L);
        reviewRequest.setUserId(1L);
    }

//    @Test
//    void testGetAll() {
//        when(reviewRepository.findAll()).thenReturn(Collections.singletonList(review));
//
//        List<ReviewResponse> result = reviewService.getAll();
//
//        assertEquals(1, result.size());
//        assertEquals("Great movie!", result.get(0).getContent());
//        verify(reviewRepository, times(1)).findAll();
//    }

    @Test
    void testAdd() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewResponse result = reviewService.add(reviewRequest);

        assertEquals("Great movie!", result.getContent());
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(movieRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(activityRepository, times(1)).save(any());
    }

    @Test
    void testGet() throws Exception {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        ReviewResponse result = reviewService.get(1L);

        assertEquals("Great movie!", result.getContent());
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdate() throws Exception {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        reviewRequest.setContent("Updated review");

        ReviewResponse result = reviewService.update(1L, reviewRequest);

        assertEquals("Updated review", result.getContent());
        verify(reviewRepository, times(1)).findById(1L);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

//    @Test
//    void testDelete() throws Exception {
//        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
//
//        reviewService.delete(1L);
//
//        verify(reviewRepository, times(1)).findById(1L);
//        verify(reviewRepository, times(1)).delete(review);
//    }

    @Test
    void testFindByMovieId() {
        when(reviewRepository.findByMovieId(1L)).thenReturn(Collections.singletonList(review));

        List<ReviewResponse> result = reviewService.findByMovieId(1L);

        assertEquals(1, result.size());
        assertEquals("Great movie!", result.get(0).getContent());
        verify(reviewRepository, times(1)).findByMovieId(1L);
    }

    @Test
    void testFindByUserAndMovie() {
        when(reviewRepository.findByUserIdAndMovieId(1L, 1L)).thenReturn(Collections.singletonList(review));

        List<ReviewResponse> result = reviewService.findByUserAndMovie(1L, 1L);

        assertEquals(1, result.size());
        assertEquals("Great movie!", result.get(0).getContent());
        verify(reviewRepository, times(1)).findByUserIdAndMovieId(1L, 1L);
    }
}
