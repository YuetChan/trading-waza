package com.tycorp.tw.domain.event;

import lombok.Getter;

import java.time.Instant;

@Getter
public class SubscriptionExpiredEvent extends AbstractDomainEntityEvent {

    private Long occuredAt = Instant.now().toEpochMilli();

    public SubscriptionExpiredEvent(Object source) {
        super(source);
    }

}
