package com.streamsphere.movieslibrary.repositories;

import com.streamsphere.movieslibrary.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByReviewId(Long reviewId);
}
