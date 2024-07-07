package com.streamsphere.movieslibrary.mappers;

import com.streamsphere.movieslibrary.entities.UserActivity;
import com.streamsphere.movieslibrary.models.requests.UserActivityRequest;
import com.streamsphere.movieslibrary.models.responses.UserActivityResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserActivityMapper {

    UserActivityMapper INSTANCE = Mappers.getMapper(UserActivityMapper.class);

    UserActivity requestToEntity(UserActivityRequest userActivityRequest);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.id", target = "userId")
    UserActivityResponse entityToResponse(UserActivity userActivity);

    List<UserActivityResponse> mapEntity(List<UserActivity> userActivities);
}
