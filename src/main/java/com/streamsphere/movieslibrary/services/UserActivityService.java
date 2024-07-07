package com.streamsphere.movieslibrary.services;

import com.streamsphere.movieslibrary.entities.enums.ActivityType;
import com.streamsphere.movieslibrary.models.requests.UserActivityRequest;
import com.streamsphere.movieslibrary.models.responses.UserActivityResponse;

import java.util.Date;
import java.util.List;

public interface UserActivityService {

    List<UserActivityResponse> getAll();

    UserActivityResponse add(UserActivityRequest userActivityRequest);

    UserActivityResponse get(Long id) throws Exception;

    UserActivityResponse update(Long id, UserActivityRequest userActivityRequest) throws Exception;

    void delete(Long id) throws Exception;

    List<UserActivityResponse> browseActivities(Long userId, ActivityType type, Date date);

    List<UserActivityResponse> getUserActivitiesAndFollowing(Long userId);
}
