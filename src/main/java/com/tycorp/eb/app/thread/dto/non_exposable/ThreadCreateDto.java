package com.tycorp.eb.app.thread.dto.non_exposable;

import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ThreadCreateDto {

    private Long processedAt;
    private Long slaveId;
    private Long userId;

    private String title;
    private String description;
    private List<String> contents;

    private Set<String> tickerNames;
    private Set<String> tagNames;

}
