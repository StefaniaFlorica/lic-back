package com.streamsphere.movieslibrary.services.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.entities.Review;
import com.streamsphere.movieslibrary.entities.enums.ItemType;
import com.streamsphere.movieslibrary.repositories.MovieListRepository;
import com.streamsphere.movieslibrary.repositories.MovieRepository;
import com.streamsphere.movieslibrary.repositories.ReviewRepository;
import com.streamsphere.movieslibrary.services.OpenAIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenAIServiceImpl implements OpenAIService {
    private final WebClient webClient;
    private final MovieRepository movieRepository;
    private final MovieListRepository movieListRepository;
    private final ReviewRepository reviewRepository;

    public OpenAIServiceImpl(@Value("${openai.api.key}") String apiKey, MovieRepository movieRepository, MovieListRepository movieListRepository, ReviewRepository reviewRepository) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
        this.movieRepository = movieRepository;
        this.movieListRepository = movieListRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Mono<String> getRecommendations(Long userId, String type) {
        List<Movie> favMovies = getFavoriteMovies(userId);
        List<Movie> allMovies = getAllMoviesByType(type);
        List<Movie> nonFavMovies = getNonFavoriteMovies(allMovies, favMovies, userId);

        String systemPrompt = readPromptFromFile("recommendation_prompt.txt");
        String userPrompt = createUserPrompt(type, favMovies, nonFavMovies);
        String requestBody = createRequestBody(systemPrompt, userPrompt);

        System.out.println("Request Body: " + requestBody);

        return this.webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractRecommendation)
                .doOnError(error -> System.out.println("Error: " + error.getMessage()));
    }

    private List<Movie> getFavoriteMovies(Long userId) {
        // Get movies rated 4 or higher
        List<Review> highRatedReviews = reviewRepository.findByUserIdAndRatingGreaterThanEqual(userId, 4);
        List<Movie> highRatedMovies = highRatedReviews.stream()
                .map(Review::getMovie)
                .distinct()
                .collect(Collectors.toList());

        // Get movies from "favorites" list
        List<Movie> favoriteMovies = movieListRepository.findByUserId(userId).stream()
                .filter(movieList -> movieList.getName().toLowerCase().contains("favorite"))
                .flatMap(movieList -> movieList.getMovies().stream())
                .collect(Collectors.toList());

        // Combine both lists and remove duplicates
        favoriteMovies.addAll(highRatedMovies);

        return favoriteMovies.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Movie> getAllMoviesByType(String type) {
        return movieRepository.findByType(ItemType.valueOf(type));
    }

    private List<Movie> getNonFavoriteMovies(List<Movie> allMovies, List<Movie> favMovies, Long userId) {
        List<Long> favMovieIds = favMovies.stream()
                .map(Movie::getId)
                .collect(Collectors.toList());

        List<Movie> watchedMovies = reviewRepository.findByUserId(userId).stream()
                .map(Review::getMovie)
                .distinct()
                .collect(Collectors.toList());

        watchedMovies.addAll(movieListRepository.findByNameAndUserId("watched", userId).stream()
                .flatMap(movieList -> movieList.getMovies().stream())
                .collect(Collectors.toList()));

        List<Long> watchedMovieIds = watchedMovies.stream()
                .map(Movie::getId)
                .collect(Collectors.toList());

        return allMovies.stream()
                .filter(movie -> !favMovieIds.contains(movie.getId()) && !watchedMovieIds.contains(movie.getId()))
                .collect(Collectors.toList());
    }

    private String createUserPrompt(String type, List<Movie> favMovies, List<Movie> nonFavMovies) {
        String favMoviesList = favMovies.stream()
                .map(movie -> String.format("{\"id\":%d, \"title\":\"%s\"}", movie.getId(), movie.getTitle()))
                .collect(Collectors.joining(", "));

        String nonFavMoviesList = nonFavMovies.stream()
                .map(movie -> String.format("{\"id\":%d, \"title\":\"%s\"}", movie.getId(), movie.getTitle()))
                .collect(Collectors.joining(", "));

        return String.format("\"%s\", [%s], [%s]", type, favMoviesList, nonFavMoviesList);
    }

    private String createRequestBody(String systemPrompt, String userPrompt) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo");
        requestBody.addProperty("max_tokens", 256);
        requestBody.addProperty("temperature", 0.7);

        JsonArray messages = new JsonArray();

        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", systemPrompt);
        messages.add(systemMessage);

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", userPrompt);
        messages.add(userMessage);

        requestBody.add("messages", messages);

        return requestBody.toString();
    }

//    private String extractRecommendation(String response) {
//        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
//        JsonObject firstChoice = jsonResponse.get("choices").getAsJsonArray().get(0).getAsJsonObject();
//        return firstChoice.get("message").getAsJsonObject().get("content").getAsString();
//    }

    private String extractRecommendation(String response) {
        // Extract the JSON response
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonObject firstChoice = jsonResponse.get("choices").getAsJsonArray().get(0).getAsJsonObject();
        String recommendationContent = firstChoice.get("message").getAsJsonObject().get("content").getAsString();

        // Parse the recommendation content to extract movie IDs and titles
        JsonArray recommendationsArray = JsonParser.parseString(recommendationContent).getAsJsonArray();

        // List to hold enriched recommendations
        JsonArray enrichedRecommendations = new JsonArray();

        // Fetch each movie's details from the database and enrich with imgUrl
        for (int i = 0; i < recommendationsArray.size(); i++) {
            JsonObject recommendedMovie = recommendationsArray.get(i).getAsJsonObject();
            Long movieId = recommendedMovie.get("id").getAsLong();

            // Fetch the movie details from the database
            Movie movie = movieRepository.findById(movieId).orElse(null);
            if (movie != null) {
                // Enrich the recommendation with imgUrl
                JsonObject enrichedMovie = new JsonObject();
                enrichedMovie.addProperty("id", movie.getId());
                enrichedMovie.addProperty("title", movie.getTitle());
                enrichedMovie.addProperty("description", recommendedMovie.get("description").getAsString());
                enrichedMovie.addProperty("imgUrl", movie.getImg());
                enrichedRecommendations.add(enrichedMovie);
            }
        }

        // Convert enriched recommendations to string
        return enrichedRecommendations.toString();
    }


    private String readPromptFromFile(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read prompt from file", e);
        }
    }

    @Override
    public Mono<String> getReviewSummary(String prompt) {
        String systemPrompt = readPromptFromFile("summarization_prompt.txt");
        String requestBody = createRequestBody(systemPrompt, prompt);

        System.out.println("Request Body: " + requestBody);

        return this.webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractSummary)
                .doOnError(error -> System.out.println("Error: " + error.getMessage()));
    }

    private String extractSummary(String response) {
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonObject firstChoice = jsonResponse.get("choices").getAsJsonArray().get(0).getAsJsonObject();
        String summary = firstChoice.get("message").getAsJsonObject().get("content").getAsString();

        return summary.trim();
    }
}
