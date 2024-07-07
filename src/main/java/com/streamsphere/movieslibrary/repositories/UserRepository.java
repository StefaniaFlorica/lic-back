package com.streamsphere.movieslibrary.repositories;

import com.streamsphere.movieslibrary.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u " +
            "ORDER BY (SELECT COUNT(r) FROM Review r WHERE r.user.id = u.id) DESC, " +
            "(SELECT COUNT(m) FROM MovieList ml JOIN ml.movies m WHERE ml.user.id = u.id) DESC")
//    @Query("SELECT u FROM AppUser u " +
//            "LEFT JOIN MovieList ml ON ml.user.id = u.id " +
//            "LEFT JOIN Review r ON r.user.id = u.id " +
//            "GROUP BY u.id " +
//            "ORDER BY (COUNT(DISTINCT r.id) + COUNT(DISTINCT ml.id)) DESC")
    List<AppUser> findTop4ActiveUsers();

    Optional<AppUser> findByUsername(String username);
}
