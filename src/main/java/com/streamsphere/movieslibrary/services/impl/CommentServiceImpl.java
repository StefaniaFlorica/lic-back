package com.streamsphere.movieslibrary.services.impl;

import com.streamsphere.movieslibrary.entities.AppUser;
import com.streamsphere.movieslibrary.entities.Comment;
import com.streamsphere.movieslibrary.entities.Review;
import com.streamsphere.movieslibrary.mappers.CommentMapper;
import com.streamsphere.movieslibrary.models.requests.CommentRequest;
import com.streamsphere.movieslibrary.models.responses.CommentResponse;
import com.streamsphere.movieslibrary.repositories.CommentRepository;
import com.streamsphere.movieslibrary.repositories.ReviewRepository;
import com.streamsphere.movieslibrary.repositories.UserRepository;
import com.streamsphere.movieslibrary.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<CommentResponse> getAll() {
        List<Comment> comments = commentRepository.findAll();

        return CommentMapper.INSTANCE.mapEntity(comments);
    }

    @Override
    public CommentResponse add(CommentRequest commentRequest) {

        Comment comment = CommentMapper.INSTANCE.requestToEntity(commentRequest);
        comment.setDate(new Date());

        Review review = reviewRepository.findById(commentRequest.getReviewId()).orElseThrow();
        AppUser user = userRepository.findById(commentRequest.getUserId()).orElseThrow();

        comment.setUser(user);
        comment.setReview(review);

        return CommentMapper.INSTANCE.entityToResponse(commentRepository.save(comment));
    }

    @Override
    public CommentResponse get(Long id) throws Exception {

        Comment comment = commentRepository.findById(id).orElseThrow();

        return CommentMapper.INSTANCE.entityToResponse(comment);
    }

    @Override
    public CommentResponse update(Long id, CommentRequest commentRequest) throws Exception {

        Comment comment = commentRepository.findById(id).orElseThrow();

        comment.setContent(commentRequest.getContent());

        return CommentMapper.INSTANCE.entityToResponse(commentRepository.save(comment));
    }

    @Override
    public void delete(Long id) throws Exception {

        Comment comment = commentRepository.findById(id).orElseThrow();

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponse> getByReview(Long reviewId) {

        List<Comment> comments = commentRepository.findByReviewId(reviewId);

        return CommentMapper.INSTANCE.mapEntity(comments);
    }
}
