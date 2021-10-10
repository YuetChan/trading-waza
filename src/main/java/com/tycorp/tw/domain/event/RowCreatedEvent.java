package com.tycorp.tw.domain.event;

import com.tycorp.tw.domain.Row;
import lombok.Getter;

import java.time.Instant;

@Getter
public class RowCreatedEvent extends AbstractDomainEntityEvent {

    private Row row;
    private Long occuredAt = Instant.now().toEpochMilli();

    public RowCreatedEvent(Object source, Row row) {
        super(source);
        this.row = row;
    }

}
