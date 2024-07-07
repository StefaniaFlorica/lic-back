package com.streamsphere.movieslibrary.controllers;

import com.streamsphere.movieslibrary.entities.AppUser;
import com.streamsphere.movieslibrary.models.requests.ForgotPasswordRequest;
import com.streamsphere.movieslibrary.models.requests.ResetPasswordRequest;
import com.streamsphere.movieslibrary.models.requests.UserRequest;
import com.streamsphere.movieslibrary.models.responses.UserResponse;
import com.streamsphere.movieslibrary.repositories.UserRepository;
import com.streamsphere.movieslibrary.security.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/")
@CrossOrigin(origins = { "http://localhost:5173" })
public class UserController {

    @Autowired
    UserService userServiceAuth;

    @Autowired
    com.streamsphere.movieslibrary.services.UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Username is already taken!");
        }
        AppUser newUser = userServiceAuth.createRegularUser(
                userRequest.getUsername(),
                userRequest.getEmail(),
                userRequest.getPassword(),
                userRequest.getQuestion(),
                userRequest.getAnswer()
        );
        newUser.setPassword("");

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAll() {

        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(userService.get(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UserRequest userRequest) throws Exception {

        return new ResponseEntity<>(userService.update(id, userRequest), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception {
        userService.delete(id);

        return new ResponseEntity<>("Deleted !", HttpStatus.NO_CONTENT);
    }

    @GetMapping("top-active")
    public ResponseEntity<List<UserResponse>> get() throws Exception {
        return new ResponseEntity<>(userService.getTop4ActiveUsers(), HttpStatus.OK);
    }

    @PostMapping("{userId}/follow/{followerId}")
    public void followUser(@PathVariable Long userId, @PathVariable Long followerId) {
        userService.followUser(userId, followerId);
    }

    @PostMapping("{userId}/unfollow/{followerId}")
    public void unfollowUser(@PathVariable Long userId, @PathVariable Long followerId) {
        userService.unfollowUser(userId, followerId);
    }

    @GetMapping("followers/{userId}")
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getFollowers(userId), HttpStatus.OK);
    }

    @GetMapping("following/{userId}")
    public ResponseEntity<List<UserResponse>> getFollowing(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getFollowing(userId), HttpStatus.OK);
    }

    @PostMapping("forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        boolean isVerified = userService.verifySecurityQuestion(forgotPasswordRequest);
        if (isVerified) {
            return ResponseEntity.ok("Security question verified. You can now reset your password.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Security question answer is incorrect");
        }
    }

    @PostMapping("reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        boolean isReset = userService.resetPassword(resetPasswordRequest);
        if (isReset) {
            return ResponseEntity.ok("Password has been reset successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or verification details");
        }
    }
}
