package com.tycorp.tw.rest.dto.exposable;

import com.google.gson.annotations.Expose;
import com.tycorp.tw.domain.Tag;
import com.tycorp.tw.domain.Ticker;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class RowGetByFilterDto {

    @Expose
    private Long processedAt;
    @Expose
    private Long userId;
    @Expose
    private String useremail;

    @Expose
    private Long rowId;

    @Expose
    private Set<Ticker> tickers;
    @Expose
    private Set<Tag> tags;

    @Expose
    private Long updatedAt;
    @Expose
    private Long updatedBy;

}
