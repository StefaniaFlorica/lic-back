package com.streamsphere.movieslibrary.repositories;

import com.streamsphere.movieslibrary.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.movie.id = :movieId ORDER BY r.date DESC")
    List<Review> findByMovieId(@Param("movieId") Long movieId);

    Integer countByMovieId(Long movieId);

    Integer countByUserId(Long userId);

    List<Review> findByUserId(Long userId);

    @Query("SELECT r FROM Review r WHERE " +
            "(:userId IS NULL OR r.user.id = :userId) AND " +
            "(:movieId IS NULL OR r.movie.id = :movieId) " +
            "ORDER BY r.date DESC")
    List<Review> findByUserIdAndMovieId(@Param("userId") Long userId, @Param("movieId") Long movieId);

    @Query("SELECT r.movie.id FROM Review r GROUP BY r.movie.id ORDER BY COUNT(r.id) DESC")
    List<Long> findTopReviewedMovies();

    @Query("SELECT r FROM Review r ORDER BY r.date DESC ")
    List<Review> findAllOrderByDateDesc();

    List<Review> findByUserIdAndRatingGreaterThanEqual(Long userId, int i);
}
