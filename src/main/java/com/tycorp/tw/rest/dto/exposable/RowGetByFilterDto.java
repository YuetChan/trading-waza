package com.tycorp.tw.rest.dto.exposable;

import com.google.gson.annotations.Expose;
import com.tycorp.tw.domain.Indicator;
import com.tycorp.tw.domain.PriceDetail;
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
    private PriceDetail priceDetail;

    @Expose
    private Ticker ticker;
    @Expose
    private Set<Indicator> indicators;

    @Expose
    private Long updatedAt;
    @Expose
    private Long updatedBy;

}
