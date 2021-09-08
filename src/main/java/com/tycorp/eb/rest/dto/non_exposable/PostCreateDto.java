package com.tycorp.eb.rest.dto.non_exposable;

import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class PostCreateDto {

    private Long processedAt;
    private Long slaveId;
    private Long userId;

    private String title;
    private String description;
    private List<String> contents;

    private Set<String> tickers;
    private Set<String> tags;

}
