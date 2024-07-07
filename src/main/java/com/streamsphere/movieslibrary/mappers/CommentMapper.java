package com.streamsphere.movieslibrary.mappers;

import com.streamsphere.movieslibrary.entities.Comment;
import com.streamsphere.movieslibrary.models.requests.CommentRequest;
import com.streamsphere.movieslibrary.models.responses.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment requestToEntity(CommentRequest commentRequest);

    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.id", target = "userId")
    CommentResponse entityToResponse(Comment comment);

    List<CommentResponse> mapEntity(List<Comment> comments);
}
