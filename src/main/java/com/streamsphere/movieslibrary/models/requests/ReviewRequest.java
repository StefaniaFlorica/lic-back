package com.streamsphere.movieslibrary.models.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {

    private String content;

    private Integer rating;

    private Long movieId;

    private Long userId;
}
