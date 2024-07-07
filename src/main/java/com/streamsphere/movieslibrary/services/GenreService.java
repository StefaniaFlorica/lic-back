package com.streamsphere.movieslibrary.services;

import com.streamsphere.movieslibrary.models.requests.GenreRequest;
import com.streamsphere.movieslibrary.models.responses.GenreResponse;

import java.util.List;

public interface GenreService {

    List<GenreResponse> getAll();

    GenreResponse add(GenreRequest genreRequest);

    GenreResponse get(Long id) throws Exception;

    GenreResponse update(Long id, GenreRequest genreRequest) throws Exception;

    void delete(Long id) throws Exception;
}
