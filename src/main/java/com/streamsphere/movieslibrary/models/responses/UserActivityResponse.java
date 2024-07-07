package com.streamsphere.movieslibrary.models.responses;

import com.streamsphere.movieslibrary.entities.enums.ActivityType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserActivityResponse {

    private Long id;

    private ActivityType type;

    private Date date;

    private String action;

    private String movieTitle;

    private Long movieId;

    private Long reviewId;

    private String username;

    private Long userId;
}
