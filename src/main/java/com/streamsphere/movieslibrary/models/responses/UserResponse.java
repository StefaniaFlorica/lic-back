package com.streamsphere.movieslibrary.models.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;

    private String username;

    private String email;

    private Integer nbFav;

    private Integer nbReview;
}
