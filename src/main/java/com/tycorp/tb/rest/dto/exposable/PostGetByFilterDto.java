package com.tycorp.tb.rest.dto.exposable;

import com.google.gson.annotations.Expose;
import com.tycorp.tb.domain.Tag;
import com.tycorp.tb.domain.Ticker;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class PostGetByFilterDto {

    @Expose
    private Long processedAt;
    @Expose
    private Long userId;
    @Expose
    private String useremail;

    @Expose
    private Long postId;

    @Expose
    private String title;
    @Expose
    private String description;

    @Expose
    private Set<Ticker> tickers;
    @Expose
    private Set<Tag> tags;

    @Expose
    private Long updatedAt;
    @Expose
    private Long updatedBy;

}
