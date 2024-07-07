package com.streamsphere.movieslibrary.mappers;

import com.streamsphere.movieslibrary.entities.MovieList;
import com.streamsphere.movieslibrary.models.requests.MovieListRequest;
import com.streamsphere.movieslibrary.models.responses.MovieListResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MovieListMapper {

    MovieListMapper INSTANCE = Mappers.getMapper(MovieListMapper.class);

    MovieList requestToEntity(MovieListRequest movieListRequest);

    @Mapping(target = "movies", source = "movies")
    MovieListResponse entityToResponse(MovieList movieList);

    List<MovieListResponse> mapEntity(List<MovieList> movieLists);
}
