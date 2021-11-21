package com.tycorp.tw.domain.event;

import lombok.Getter;

import java.time.Instant;

@Getter
public class SubscriptionCreatedEvent extends AbstractDomainEntityEvent {

    private Long occuredAt = Instant.now().toEpochMilli();

    public SubscriptionCreatedEvent(Object source) {
        super(source);
    }

}
