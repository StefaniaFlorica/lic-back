package com.streamsphere.movieslibrary.services;

import com.streamsphere.movieslibrary.models.requests.CommentRequest;
import com.streamsphere.movieslibrary.models.responses.CommentResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getAll();

    CommentResponse add(CommentRequest commentRequest);

    CommentResponse get(Long id) throws Exception;

    CommentResponse update(Long id, CommentRequest commentRequest) throws Exception;

    void delete(Long id) throws Exception;

    List<CommentResponse> getByReview(Long reviewId);
}
