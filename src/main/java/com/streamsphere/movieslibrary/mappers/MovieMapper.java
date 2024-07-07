package com.streamsphere.movieslibrary.mappers;

import com.streamsphere.movieslibrary.entities.Movie;
import com.streamsphere.movieslibrary.models.requests.MovieRequest;
import com.streamsphere.movieslibrary.models.responses.MovieResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    Movie requestToEntity(MovieRequest movieRequest);

    @Mapping(source = "genre.title", target = "genreTitle")
    @Mapping(source = "genre.id", target = "genreId")
    MovieResponse entityToResponse(Movie movie);

    List<MovieResponse> mapEntity(List<Movie> movies);
}
