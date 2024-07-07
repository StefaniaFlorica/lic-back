package com.streamsphere.movieslibrary.models.requests;

import com.streamsphere.movieslibrary.entities.enums.ActivityType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserActivityRequest {

    private ActivityType type;

    private Date date;

    private Long userId;
}
