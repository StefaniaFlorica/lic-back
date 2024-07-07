package com.streamsphere.movieslibrary.services;

import com.streamsphere.movieslibrary.models.requests.ReviewRequest;
import com.streamsphere.movieslibrary.models.responses.ReviewResponse;

import java.util.List;

public interface ReviewService {

    List<ReviewResponse> getAll();

    ReviewResponse add(ReviewRequest reviewRequest);

    ReviewResponse get(Long id) throws Exception;

    ReviewResponse update(Long id, ReviewRequest reviewRequest) throws Exception;

    void delete(Long id) throws Exception;

    List<ReviewResponse> findByMovieId(Long movieId);

    List<ReviewResponse> findByUserAndMovie(Long userId, Long movieId);
}
