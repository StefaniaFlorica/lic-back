package com.streamsphere.movieslibrary.repositories;

import com.streamsphere.movieslibrary.entities.Cast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CastRepository extends JpaRepository<Cast, Long> {

    List<Cast> findByMovieId(Long movieId);
}
