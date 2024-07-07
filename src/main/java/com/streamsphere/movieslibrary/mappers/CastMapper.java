package com.streamsphere.movieslibrary.mappers;

import com.streamsphere.movieslibrary.entities.Cast;
import com.streamsphere.movieslibrary.models.requests.CastRequest;
import com.streamsphere.movieslibrary.models.responses.CastResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CastMapper {

    CastMapper INSTANCE = Mappers.getMapper(CastMapper.class);

    Cast requestToEntity(CastRequest castRequest);

    CastResponse entityToResponse(Cast cast);

    List<CastResponse> mapEntity(List<Cast> casts);

    List<Cast> mapRequests(List<CastRequest> castRequests);
}
