package com.streamsphere.movieslibrary.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReviewResponse {

    private Long id;

    private String content;

    private Integer rating;

    private Date date;

    private Long userId;

    private String username;

    private String movieTitle;

    private Long movieId;
}
