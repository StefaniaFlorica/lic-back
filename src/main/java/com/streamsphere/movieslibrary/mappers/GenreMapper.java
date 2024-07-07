package com.streamsphere.movieslibrary.mappers;

import com.streamsphere.movieslibrary.entities.Genre;
import com.streamsphere.movieslibrary.models.requests.GenreRequest;
import com.streamsphere.movieslibrary.models.responses.GenreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GenreMapper {

    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    Genre requestToEntity(GenreRequest genreRequest);

    GenreResponse entityToResponse(Genre genre);

    List<GenreResponse> mapEntity(List<Genre> genres);
}
