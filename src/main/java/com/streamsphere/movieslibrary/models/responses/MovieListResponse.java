package com.streamsphere.movieslibrary.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieListResponse {

    private Long id;

    private String name;

    private List<MovieResponse> movies;
}
