package com.tycorp.eb.rest.dto.transformer;

import com.tycorp.eb.rest.dto.exposable.PostGetByFilterDto;
import com.tycorp.eb.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostGetByFilterDtoTransformer {

    PostGetByFilterDtoTransformer INSTANCE = Mappers.getMapper(PostGetByFilterDtoTransformer.class);

    @Mapping(source = "processedAt", target = "processedAt")
    @Mapping(expression="java(post.getUser().getUserId())", target = "userId")
    @Mapping(expression="java(post.getUser().getUseremail())", target = "useremail")
    @Mapping(source = "postId", target = "postId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "tickers", target = "tickers")
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "updatedBy", target = "updatedBy")
    PostGetByFilterDto transform(Post post);

}
