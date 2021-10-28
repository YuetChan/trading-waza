package com.tycorp.tw.domain;

import com.google.gson.annotations.Expose;
import lombok.*;

import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Embeddable
public class PriceDetail {

    @Expose
    private Double open;
    @Expose
    private Double high;
    @Expose
    private Double close;
    @Expose
    private Double low;

    @Expose
    private Double change;

}
