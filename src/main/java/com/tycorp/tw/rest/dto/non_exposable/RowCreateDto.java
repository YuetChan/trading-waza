package com.tycorp.tw.rest.dto.non_exposable;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class RowCreateDto {

    @NotNull
    private Long processedAt;
    @NotNull
    private Long slaveId;
    @NotNull
    private Long userId;

    @NotNull
    private String ticker;
    @NotNull
    private Set<String> indicators;

}
