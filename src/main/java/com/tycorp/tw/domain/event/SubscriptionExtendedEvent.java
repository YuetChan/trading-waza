package com.tycorp.tw.domain.event;

import lombok.Getter;

import java.time.Instant;

@Getter
public class SubscriptionExtendedEvent extends AbstractDomainEntityEvent {

    private Long occuredAt = Instant.now().toEpochMilli();

    public SubscriptionExtendedEvent(Object source) {
        super(source);
    }

}
