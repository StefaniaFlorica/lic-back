package com.streamsphere.movieslibrary.services.impl;

import com.streamsphere.movieslibrary.entities.Genre;
import com.streamsphere.movieslibrary.mappers.GenreMapper;
import com.streamsphere.movieslibrary.models.requests.GenreRequest;
import com.streamsphere.movieslibrary.models.responses.GenreResponse;
import com.streamsphere.movieslibrary.repositories.GenreRepository;
import com.streamsphere.movieslibrary.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    GenreRepository genreRepository;

    @Override
    public List<GenreResponse> getAll() {
        List<Genre> genres = genreRepository.findAll();

        return GenreMapper.INSTANCE.mapEntity(genres);
    }

    @Override
    public GenreResponse add(GenreRequest genreRequest) {

        Genre genre = GenreMapper.INSTANCE.requestToEntity(genreRequest);

        return GenreMapper.INSTANCE.entityToResponse(genreRepository.save(genre));
    }

    @Override
    public GenreResponse get(Long id) throws Exception {

        Genre genre = genreRepository.findById(id).orElseThrow();

        return GenreMapper.INSTANCE.entityToResponse(genre);
    }

    @Override
    public GenreResponse update(Long id, GenreRequest genreRequest) throws Exception {

        Genre genre = genreRepository.findById(id).orElseThrow();

        genre.setTitle(genreRequest.getTitle());

        return GenreMapper.INSTANCE.entityToResponse(genreRepository.save(genre));
    }

    @Override
    public void delete(Long id) throws Exception {
        Genre genre = genreRepository.findById(id).orElseThrow();

        genreRepository.delete(genre);
    }
}
