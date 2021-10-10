package com.tycorp.tw.rest.dto.transformer;

import com.tycorp.tw.domain.Row;
import com.tycorp.tw.rest.dto.exposable.RowGetByFilterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RowGetByFilterDtoTransformer {

    RowGetByFilterDtoTransformer INSTANCE = Mappers.getMapper(RowGetByFilterDtoTransformer.class);

    @Mapping(source = "processedAt", target = "processedAt")
    @Mapping(expression="java(row.getUser().getUserId())", target = "userId")
    @Mapping(expression="java(row.getUser().getUseremail())", target = "useremail")
    @Mapping(source = "rowId", target = "rowId")
    @Mapping(source = "ticker", target = "ticker")
    @Mapping(source = "indicators", target = "indicators")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "updatedBy", target = "updatedBy")
    RowGetByFilterDto transform(Row row);

}
