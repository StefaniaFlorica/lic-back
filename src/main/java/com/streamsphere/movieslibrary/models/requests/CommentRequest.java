package com.streamsphere.movieslibrary.models.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    private String content;

    private Long reviewId;

    private Long userId;
}
