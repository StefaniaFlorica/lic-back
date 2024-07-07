package com.streamsphere.movieslibrary.services.impl;

import com.streamsphere.movieslibrary.entities.AppUser;
import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.entities.Review;
import com.streamsphere.movieslibrary.entities.UserActivity;
import com.streamsphere.movieslibrary.entities.enums.ActivityType;
import com.streamsphere.movieslibrary.mappers.ReviewMapper;
import com.streamsphere.movieslibrary.models.requests.ReviewRequest;
import com.streamsphere.movieslibrary.models.responses.ReviewResponse;
import com.streamsphere.movieslibrary.repositories.MovieRepository;
import com.streamsphere.movieslibrary.repositories.ReviewRepository;
import com.streamsphere.movieslibrary.repositories.UserActivityRepository;
import com.streamsphere.movieslibrary.repositories.UserRepository;
import com.streamsphere.movieslibrary.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserActivityRepository activityRepository;

    @Override
    public List<ReviewResponse> getAll() {

        List<Review> reviews = reviewRepository.findAllOrderByDateDesc();

        return ReviewMapper.INSTANCE.mapEntity(reviews);
    }

    @Override
    public ReviewResponse add(ReviewRequest reviewRequest) {

        Review review = ReviewMapper.INSTANCE.requestToEntity(reviewRequest);

        Movie movie = movieRepository.findById(reviewRequest.getMovieId()).orElseThrow();
        AppUser user = userRepository.findById(reviewRequest.getUserId()).orElseThrow();

        review.setDate(new Date());
        review.setMovie(movie);
        review.setUser(user);

        reviewRepository.save(review);

        updateMovieRating(movie);

        UserActivity activity = UserActivity.builder()
                .date(new Date())
                .type(ActivityType.Review)
                .action("Add")
                .user(user)
                .movieTitle(movie.getTitle())
                .movieId(movie.getId())
                .reviewId(review.getId())
                .build();

        activityRepository.save(activity);

        return ReviewMapper.INSTANCE.entityToResponse(review);
    }

    private void updateMovieRating(Movie movie) {
        List<Review> reviews = reviewRepository.findByMovieId(movie.getId());
        float newRating = (float) reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
        movie.setRating(newRating);
        movieRepository.save(movie);
    }

    @Override
    public ReviewResponse get(Long id) throws Exception {

        Review review = reviewRepository.findById(id).orElseThrow();

        return ReviewMapper.INSTANCE.entityToResponse(review);
    }

    @Override
    public ReviewResponse update(Long id, ReviewRequest reviewRequest) throws Exception {

        Review review = reviewRepository.findById(id).orElseThrow();

        review.setContent(reviewRequest.getContent());
        review.setRating(reviewRequest.getRating());

        return ReviewMapper.INSTANCE.entityToResponse(reviewRepository.save(review));
    }

    @Override
    public void delete(Long id) throws Exception {

        Review review = reviewRepository.findById(id).orElseThrow();
        Movie movie = movieRepository.findById(review.getMovie().getId()).orElseThrow();

        reviewRepository.delete(review);

        updateMovieRating(movie);
    }

    @Override
    public List<ReviewResponse> findByMovieId(Long movieId) {

        List<Review> reviews = reviewRepository.findByMovieId(movieId);

        return ReviewMapper.INSTANCE.mapEntity(reviews);
    }

    @Override
    public List<ReviewResponse> findByUserAndMovie(Long userId, Long movieId) {

        List<Review> reviews = reviewRepository.findByUserIdAndMovieId(userId, movieId);

        return ReviewMapper.INSTANCE.mapEntity(reviews);
    }
}
