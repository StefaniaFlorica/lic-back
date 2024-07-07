package com.streamsphere.movieslibrary.security;

import com.streamsphere.movieslibrary.entities.AppUser;
import com.streamsphere.movieslibrary.models.requests.MovieListRequest;
import com.streamsphere.movieslibrary.repositories.UserRepository;
import com.streamsphere.movieslibrary.services.impl.MovieListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieListServiceImpl movieListService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AppUser createAdminUser(
            String username,
            String email,
            String password,
            String question,
            String answer
    ) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.getRoles().add("ROLE_ADMIN");
        user.setQuestion(question);
        user.setAnswer(answer);
        return userRepository.save(user);
    }

    public AppUser createRegularUser(
            String username,
            String email,
            String password,
            String question,
            String answer
            ) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setQuestion(question);
        user.setAnswer(answer);
        user.getRoles().add("ROLE_USER");
        final AppUser savedUser = userRepository.save(user);
        MovieListRequest movieListRequest = new MovieListRequest("favorites", savedUser.getId());
        movieListService.add(movieListRequest);
        return savedUser;
    }
}
