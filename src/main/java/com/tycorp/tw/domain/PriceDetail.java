package com.tycorp.tw.domain;

import lombok.*;

import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Embeddable
public class PriceDetail {

    private Double open;
    private Double high;
    private Double close;
    private Double low;

    private Double change;

}
