package com.streamsphere.movieslibrary.models.requests;

import com.streamsphere.movieslibrary.entities.enums.ItemType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MovieRequest {

    private String title;

    private String resume;

    private ItemType type;

    private String img;

    private Date releaseDate;

    private String director;

    private Long genreId;

    private List<CastRequest> casts;
}
