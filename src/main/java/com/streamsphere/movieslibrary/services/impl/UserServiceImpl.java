package com.streamsphere.movieslibrary.services.impl;

import com.streamsphere.movieslibrary.entities.AppUser;
import com.streamsphere.movieslibrary.mappers.UserMapper;
import com.streamsphere.movieslibrary.models.requests.ForgotPasswordRequest;
import com.streamsphere.movieslibrary.models.requests.MovieListRequest;
import com.streamsphere.movieslibrary.models.requests.ResetPasswordRequest;
import com.streamsphere.movieslibrary.models.requests.UserRequest;
import com.streamsphere.movieslibrary.models.responses.UserResponse;
import com.streamsphere.movieslibrary.repositories.MovieListRepository;
import com.streamsphere.movieslibrary.repositories.ReviewRepository;
import com.streamsphere.movieslibrary.repositories.UserRepository;
import com.streamsphere.movieslibrary.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MovieListRepository listRepository;

    @Autowired
    MovieListServiceImpl movieListService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAll() {

        List<AppUser> users = userRepository.findAll();

        return UserMapper.INSTANCE.mapEntity(users);
    }

    @Override
    public UserResponse add(UserRequest userRequest) {

        AppUser user = UserMapper.INSTANCE.requestToEntity(userRequest);

        return UserMapper.INSTANCE.entityToResponse(userRepository.save(user));
    }

    @Override
    public UserResponse get(Long id) throws Exception {

        AppUser user = userRepository.findById(id).orElseThrow();

        UserResponse response = UserMapper.INSTANCE.entityToResponse(user);

        response.setNbReview(reviewRepository.countByUserId(id));
        response.setNbFav(listRepository.countTotalMoviesByUserId(id));

        return response;
    }

    @Override
    public UserResponse update(Long id, UserRequest userRequest) throws Exception {

        AppUser user = userRepository.findById(id).orElseThrow();

        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setUsername(userRequest.getUsername());

        return UserMapper.INSTANCE.entityToResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) throws Exception {

        AppUser user = userRepository.findById(id).orElseThrow();

        userRepository.delete(user);
    }

    @Override
    public List<UserResponse> getTop4ActiveUsers() {
        List<AppUser> topUsers = userRepository.findTop4ActiveUsers();

        return topUsers.stream().limit(5).map(user -> {
            UserResponse response = UserMapper.INSTANCE.entityToResponse(user);

            response.setNbReview(reviewRepository.countByUserId(user.getId()));
            response.setNbFav(listRepository.countTotalMoviesByUserId(user.getId()));

            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public void followUser(Long userId, Long followerId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);
        Optional<AppUser> followerOpt = userRepository.findById(followerId);

        if (userOpt.isPresent() && followerOpt.isPresent()) {
            AppUser user = userOpt.get();
            AppUser follower = followerOpt.get();
            user.getFollowing().add(follower);
            follower.getFollowers().add(user);
            userRepository.save(user);
            userRepository.save(follower);
        }
    }

    @Override
    public void unfollowUser(Long userId, Long followerId) {
        Optional<AppUser> userOpt = userRepository.findById(userId);
        Optional<AppUser> followerOpt = userRepository.findById(followerId);

        if (userOpt.isPresent() && followerOpt.isPresent()) {
            AppUser user = userOpt.get();
            AppUser follower = followerOpt.get();
            user.getFollowing().remove(follower);
            follower.getFollowers().remove(user);
            userRepository.save(user);
            userRepository.save(follower);
        }
    }

    @Override
    public List<UserResponse> getFollowers(Long userId) {
        List<AppUser> users = userRepository.findById(userId)
                .map(AppUser::getFollowers)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .stream().toList();

        return UserMapper.INSTANCE.mapEntity(users);
    }

    @Override
    public List<UserResponse> getFollowing(Long userId) {
        List<AppUser> users = userRepository.findById(userId)
                .map(AppUser::getFollowing)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .stream().toList();;

        return UserMapper.INSTANCE.mapEntity(users);
    }

    @Override
    public boolean verifySecurityQuestion(ForgotPasswordRequest request) {
        Optional<AppUser> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();

            return user.getQuestion().equals(request.getQuestion())
                    && user.getAnswer().equals(request.getAnswer());
        }
        return false;
    }

    @Override
    public boolean resetPassword(ResetPasswordRequest request) {
        Optional<AppUser> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
