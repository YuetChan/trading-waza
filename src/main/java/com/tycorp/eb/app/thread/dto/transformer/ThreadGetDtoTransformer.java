package com.tycorp.eb.app.thread.dto.transformer;

import com.tycorp.eb.app.thread.dto.exposable.ThreadGetDto;
import com.tycorp.eb.domain.thread.model.Thread;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ThreadGetDtoTransformer {

    ThreadGetDtoTransformer INSTANCE = Mappers.getMapper(ThreadGetDtoTransformer.class);

    @Mapping(source = "processedAt", target = "processedAt")
    @Mapping(expression="java(thread.getUser().getUserId())", target = "userId")
    @Mapping(expression="java(thread.getUser().getUseremail())", target = "useremail")
    @Mapping(source = "threadId", target = "threadId")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "contents", target = "contents")
    @Mapping(source = "tickers", target = "tickers")
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "updatedBy", target = "updatedBy")
    ThreadGetDto transform(Thread thread);

}
