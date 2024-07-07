package com.streamsphere.movieslibrary.repositories;

import com.streamsphere.movieslibrary.entities.UserActivity;
import com.streamsphere.movieslibrary.entities.enums.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    @Query("SELECT a from UserActivity a WHERE " +
            "(:userId IS NULL OR a.user.id = :userId) AND " +
            "(:type IS NULL OR a.type = :type) AND " +
            "(:date IS NULL OR FUNCTION('DATE', a.date) = :date) " +
            "ORDER BY a.date DESC")
    List<UserActivity> findActivities(Long userId, ActivityType type, Date date);

    @Query("SELECT a FROM UserActivity a WHERE " +
            "(a.user.id IN (SELECT f.id FROM AppUser u JOIN u.following f WHERE u.id = :userId)) " +
            "ORDER BY a.date DESC")
    List<UserActivity> findActivitiesFollowing(Long userId);
}
