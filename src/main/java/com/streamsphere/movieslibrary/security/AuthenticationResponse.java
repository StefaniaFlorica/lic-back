package com.streamsphere.movieslibrary.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

    private final String jwt;
    private final String username;
    private final Long id;
    private final List<String> roles;

}
