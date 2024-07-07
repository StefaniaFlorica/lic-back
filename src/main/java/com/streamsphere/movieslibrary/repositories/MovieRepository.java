package com.streamsphere.movieslibrary.repositories;

import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.entities.enums.ItemType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m from Movie m WHERE " +
            "m.title LIKE %:title% " +
            "AND m.genre.title LIKE %:genreTitle% " +
            "AND (:type IS NULL OR m.type = :type)")
    List<Movie> browseMovies(String title, String genreTitle, ItemType type);

    // Top-rated
    List<Movie> findTop6ByOrderByRatingDesc(Pageable pageable);

    // Trending Movies / TV Shows
    List<Movie> findTop6ByTypeOrderByReleaseDateDesc(ItemType type, Pageable pageable);

    List<Movie> findByType(ItemType type);
}
