package com.streamsphere.movieslibrary.services;

import com.streamsphere.movieslibrary.models.requests.ForgotPasswordRequest;
import com.streamsphere.movieslibrary.models.requests.ResetPasswordRequest;
import com.streamsphere.movieslibrary.models.requests.UserRequest;
import com.streamsphere.movieslibrary.models.responses.UserResponse;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<UserResponse> getAll();

    UserResponse add(UserRequest userRequest);

    UserResponse get(Long id) throws Exception;

    UserResponse update(Long id, UserRequest userRequest) throws Exception;

    void delete(Long id) throws Exception;

    List<UserResponse> getTop4ActiveUsers();

    void followUser(Long userId, Long followerId);

    void unfollowUser(Long userId, Long followerId);

    List<UserResponse> getFollowers(Long userId);

    List<UserResponse> getFollowing(Long userId);

    boolean verifySecurityQuestion(ForgotPasswordRequest forgotPasswordRequest);

    boolean resetPassword(ResetPasswordRequest resetPasswordRequest);

}
