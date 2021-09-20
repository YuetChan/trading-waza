package com.tycorp.tb.rest.dto.non_exposable;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class PostCreateDto {

    @NotNull
    private Long processedAt;
    @NotNull
    private Long slaveId;
    @NotNull
    private Long userId;

    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private List<String> contents;

    @NotNull
    private Set<String> tickers;
    @NotNull
    private Set<String> tags;

}
