package com.tycorp.eb.app.thread.dto.non_exposable;

import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class ThreadUpdateDto {

    private Long threadId;

    private String title;
    private String description;
    private List<String> contents;

    private Set<String> addedTagNames;
    private Set<String> addedTickerNames;

    private Set<String> removedTagNames;
    private Set<String> removedTickerNames;

}
