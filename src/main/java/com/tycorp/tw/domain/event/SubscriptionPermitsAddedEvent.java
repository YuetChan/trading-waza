package com.tycorp.tw.domain.event;

import lombok.Getter;

import java.time.Instant;

@Getter
public class SubscriptionPermitsAddedEvent extends AbstractDomainEntityEvent {

    private Long occuredAt = Instant.now().toEpochMilli();

    public SubscriptionPermitsAddedEvent(Object source) {
        super(source);
    }

}
