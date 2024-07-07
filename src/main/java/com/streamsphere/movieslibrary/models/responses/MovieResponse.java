package com.streamsphere.movieslibrary.models.responses;

import com.streamsphere.movieslibrary.entities.enums.ItemType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MovieResponse {

    private Long id;

    private String title;

    private String resume;

    private ItemType type;

    private Float rating;

    private Integer nbReviews;

    private Integer nbFav;

    private String img;

    private Date releaseDate;

    private String director;

    private Long genreId;

    private String genreTitle;
}
