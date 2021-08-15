package com.tycorp.eb.app.thread.dto.exposable;

import com.google.gson.annotations.Expose;
import com.tycorp.eb.domain.meta_data.model.Tag;
import com.tycorp.eb.domain.meta_data.model.Ticker;
import com.tycorp.eb.domain.thread.model.*;
import com.tycorp.eb.domain.user.model.EbUser;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ThreadGetByFilterDto {

    @Expose
    private Long processedAt;
    @Expose
    private Long userId;
    @Expose
    private String useremail;

    @Expose
    private Long threadId;

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
