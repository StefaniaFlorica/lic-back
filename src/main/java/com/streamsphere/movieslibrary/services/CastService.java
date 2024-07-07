package com.streamsphere.movieslibrary.services;

import com.streamsphere.movieslibrary.models.requests.CastRequest;
import com.streamsphere.movieslibrary.models.responses.CastResponse;

import java.util.List;

public interface CastService {

    List<CastResponse> getAll();

    CastResponse add(CastRequest castRequest);

    CastResponse get(Long id) throws Exception;

    CastResponse update(Long id, CastRequest castRequest) throws Exception;

    void delete(Long id) throws Exception;

    List<CastResponse> getByMovie(Long movieId) throws Exception;
}
