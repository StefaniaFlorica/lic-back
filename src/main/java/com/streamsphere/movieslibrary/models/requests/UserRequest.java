package com.streamsphere.movieslibrary.models.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    private String username;

    private String email;

    private String password;

    private String question;

    private String answer;
}
