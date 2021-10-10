package com.tycorp.tw.domain;

import lombok.*;

import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@Embeddable
public class PriceDetail {

    private Long open;
    private Long high;
    private Long close;
    private Long low;

    private Long change;

}
