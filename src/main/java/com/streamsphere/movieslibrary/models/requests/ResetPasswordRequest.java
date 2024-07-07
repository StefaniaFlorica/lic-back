package com.streamsphere.movieslibrary.models.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    private String username;

    private String newPassword;
}
