package com.streamsphere.movieslibrary.init;

import com.streamsphere.movieslibrary.entities.Genre;
import com.streamsphere.movieslibrary.repositories.GenreRepository;
import com.streamsphere.movieslibrary.repositories.UserRepository;
import com.streamsphere.movieslibrary.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                userService.createAdminUser(
                        "admin",
                        "admin@email.com",
                        "admin1234",
                        "What is your favorite movie?",
                        "test"
                );
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                userService.createRegularUser(
                        "user",
                        "user@email.com",
                        "user1234",
                        "What is your favorite movie?",
                        "test"
                        );
            }

            List<Genre> genres = List.of(
                    new Genre(null, "Action", "https://dox9s3cfvvyi4.cloudfront.net/genres/movies/action.png", null),
                    new Genre(null, "Adventure", "https://dox9s3cfvvyi4.cloudfront.net/genres/movies/adventure.png", null),
                    new Genre(null, "Comedy", "https://dox9s3cfvvyi4.cloudfront.net/genres/movies/comedy.png", null),
                    new Genre(null, "Drama", "https://dox9s3cfvvyi4.cloudfront.net/genres/movies/drama.png", null),
                    new Genre(null, "Horror", "https://dox9s3cfvvyi4.cloudfront.net/genres/movies/horror.png", null),
                    new Genre(null, "Romance", "https://dox9s3cfvvyi4.cloudfront.net/genres/movies/romance.png", null),
                    new Genre(null, "Talk show", "https://dox9s3cfvvyi4.cloudfront.net/genres/talkShow.png", null),
                    new Genre(null, "Family", "https://dox9s3cfvvyi4.cloudfront.net/genres/movies/family.png", null),
                    new Genre(null, "Sport", "https://dox9s3cfvvyi4.cloudfront.net/genres/movies/sport.png", null),
                    new Genre(null, "Suspense", "https://dox9s3cfvvyi4.cloudfront.net/genres/movies/suspense.png", null),
                    new Genre(null, "History", "https://dox9s3cfvvyi4.cloudfront.net/genres/movies/history.png", null),
                    new Genre(null, "Travel", "https://dox9s3cfvvyi4.cloudfront.net/genres/travel.png", null),
                    new Genre(null, "Thriller", "https://i.pinimg.com/236x/71/3c/bd/713cbd0590734a208fe5e8796715a6cf.jpg", null)
            );

            for(Genre genre : genres) {
                if (genreRepository.findByTitle(genre.getTitle()).isEmpty()) {
                    genreRepository.save(genre);
                }
            }
        };
    }
}
