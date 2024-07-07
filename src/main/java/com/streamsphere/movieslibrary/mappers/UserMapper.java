package com.streamsphere.movieslibrary.mappers;

import com.streamsphere.movieslibrary.entities.AppUser;
import com.streamsphere.movieslibrary.models.requests.UserRequest;
import com.streamsphere.movieslibrary.models.responses.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    AppUser requestToEntity(UserRequest UserRequest);

    UserResponse entityToResponse(AppUser appUser);

    List<UserResponse> mapEntity(List<AppUser> AppUser);
}
