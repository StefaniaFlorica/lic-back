package com.streamsphere.movieslibrary.models.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest {

    private String username;

    private String question;

    private String answer;
}
