package com.streamsphere.movieslibrary.repositories;

import com.streamsphere.movieslibrary.entities.MovieList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieListRepository  extends JpaRepository<MovieList, Long> {

    List<MovieList> findByUserId(Long userId);

    @Query("SELECT COUNT(m) FROM MovieList ml JOIN ml.movies m WHERE ml.user.id = :userId")
    int countTotalMoviesByUserId(Long userId);

    @Query("SELECT COUNT(ml) FROM MovieList ml JOIN ml.movies m WHERE m.id = :movieId")
    int countMovieInAllMovieLists(Long movieId);

    @Query("SELECT m.id FROM MovieList ml JOIN ml.movies m GROUP BY m.id ORDER BY COUNT(m.id) DESC")
    List<Long> findTopMoviesInLists();
    Optional<MovieList> findByNameAndUserId(String name, Long userId);

}
