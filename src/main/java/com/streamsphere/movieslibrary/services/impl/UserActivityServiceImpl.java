package com.streamsphere.movieslibrary.services.impl;

import com.streamsphere.movieslibrary.entities.AppUser;
import com.streamsphere.movieslibrary.entities.UserActivity;
import com.streamsphere.movieslibrary.entities.enums.ActivityType;
import com.streamsphere.movieslibrary.mappers.UserActivityMapper;
import com.streamsphere.movieslibrary.models.requests.UserActivityRequest;
import com.streamsphere.movieslibrary.models.responses.UserActivityResponse;
import com.streamsphere.movieslibrary.repositories.UserActivityRepository;
import com.streamsphere.movieslibrary.repositories.UserRepository;
import com.streamsphere.movieslibrary.services.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserActivityServiceImpl implements UserActivityService {

    @Autowired
    UserActivityRepository userActivityRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserActivityResponse> getAll() {
        List<UserActivity> activities = userActivityRepository.findAll();

        return UserActivityMapper.INSTANCE.mapEntity(activities);
    }

    @Override
    public UserActivityResponse add(UserActivityRequest userActivityRequest) {

        UserActivity userActivity = UserActivityMapper.INSTANCE.requestToEntity(userActivityRequest);

        AppUser user = userRepository.findById(userActivityRequest.getUserId()).orElseThrow();

        userActivity.setUser(user);

        return UserActivityMapper.INSTANCE.entityToResponse(userActivityRepository.save(userActivity));
    }

    @Override
    public UserActivityResponse get(Long id) throws Exception {

        UserActivity userActivity = userActivityRepository.findById(id).orElseThrow();

        return UserActivityMapper.INSTANCE.entityToResponse(userActivity);
    }

    @Override
    public UserActivityResponse update(Long id, UserActivityRequest userActivityRequest) throws Exception {

        UserActivity userActivity = userActivityRepository.findById(id).orElseThrow();

        userActivity.setDate(userActivityRequest.getDate());
        userActivity.setType(userActivityRequest.getType());

        return UserActivityMapper.INSTANCE.entityToResponse(userActivityRepository.save(userActivity));
    }

    @Override
    public void delete(Long id) throws Exception {

        UserActivity userActivity = userActivityRepository.findById(id).orElseThrow();

        userActivityRepository.delete(userActivity);
    }

    @Override
    public List<UserActivityResponse> browseActivities(Long userId, ActivityType type, Date date) {

        List<UserActivity> activities = userActivityRepository.findActivities(userId, type, date);

        return UserActivityMapper.INSTANCE.mapEntity(activities);
    }

    @Override
    public List<UserActivityResponse> getUserActivitiesAndFollowing(Long userId) {

        List<UserActivity> activities = userActivityRepository.findActivitiesFollowing(userId);

        return UserActivityMapper.INSTANCE.mapEntity(activities);
    }
}
