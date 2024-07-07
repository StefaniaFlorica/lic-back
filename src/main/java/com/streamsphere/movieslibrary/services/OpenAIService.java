package com.streamsphere.movieslibrary.services;

import reactor.core.publisher.Mono;

public interface OpenAIService {

    public Mono<String> getReviewSummary(String prompt);

    public Mono<String> getRecommendations(Long userId, String type);
}

