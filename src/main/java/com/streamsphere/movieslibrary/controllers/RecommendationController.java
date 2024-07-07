package com.streamsphere.movieslibrary.controllers;

import com.streamsphere.movieslibrary.services.MovieService;
import com.streamsphere.movieslibrary.services.impl.MovieServiceImpl;
import com.streamsphere.movieslibrary.services.impl.OpenAIServiceImpl;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/recommendations/")
@CrossOrigin(origins = {"http://localhost:5173"})
public class RecommendationController {
    private final OpenAIServiceImpl openAIServiceImpl;

    public RecommendationController(OpenAIServiceImpl openAIServiceImpl) {
        this.openAIServiceImpl = openAIServiceImpl;
    }

    @GetMapping
    public Mono<String> fetchRecommendations(@RequestParam Long userId, @RequestParam String type) {

        return openAIServiceImpl.getRecommendations(userId, type);
    }
}
