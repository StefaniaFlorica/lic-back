package com.streamsphere.movieslibrary.services.impl;

import com.streamsphere.movieslibrary.entities.Cast;
import com.streamsphere.movieslibrary.mappers.CastMapper;
import com.streamsphere.movieslibrary.models.requests.CastRequest;
import com.streamsphere.movieslibrary.models.responses.CastResponse;
import com.streamsphere.movieslibrary.repositories.CastRepository;
import com.streamsphere.movieslibrary.services.CastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CastServiceImpl implements CastService {

    @Autowired
    CastRepository castRepository;

    @Override
    public List<CastResponse> getAll() {
        List<Cast> casts = castRepository.findAll();

        return CastMapper.INSTANCE.mapEntity(casts);
    }

    @Override
    public CastResponse add(CastRequest castRequest) {

        Cast cast = CastMapper.INSTANCE.requestToEntity(castRequest);

        return CastMapper.INSTANCE.entityToResponse(castRepository.save(cast));
    }

    @Override
    public CastResponse get(Long id) throws Exception {

        Cast cast = castRepository.findById(id).orElseThrow();

        return CastMapper.INSTANCE.entityToResponse(cast);
    }

    @Override
    public CastResponse update(Long id, CastRequest castRequest) throws Exception {

        Cast cast = castRepository.findById(id).orElseThrow();

        cast.setActorName(castRequest.getActorName());
        cast.setName(castRequest.getName());


        return CastMapper.INSTANCE.entityToResponse(castRepository.save(cast));
    }

    @Override
    public void delete(Long id) throws Exception {
        Cast cast = castRepository.findById(id).orElseThrow();

        castRepository.delete(cast);
    }

    @Override
    public List<CastResponse> getByMovie(Long movieId) throws Exception {

        List<Cast> casts = castRepository.findByMovieId(movieId);

        return CastMapper.INSTANCE.mapEntity(casts);
    }
}
