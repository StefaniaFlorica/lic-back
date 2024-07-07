package com.streamsphere.movieslibrary.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentResponse {

    private Long id;

    private String content;

    private Date date;

    private String username;

    private Long userId;
}
