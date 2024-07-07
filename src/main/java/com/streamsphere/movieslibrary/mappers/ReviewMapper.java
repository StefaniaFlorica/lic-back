package com.streamsphere.movieslibrary.mappers;

import com.streamsphere.movieslibrary.entities.Review;
import com.streamsphere.movieslibrary.models.requests.ReviewRequest;
import com.streamsphere.movieslibrary.models.responses.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    Review requestToEntity(ReviewRequest reviewRequest);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "movie.title", target = "movieTitle")
    @Mapping(source = "movie.id", target = "movieId")
    ReviewResponse entityToResponse(Review review);

    List<ReviewResponse> mapEntity(List<Review> reviews);
}
