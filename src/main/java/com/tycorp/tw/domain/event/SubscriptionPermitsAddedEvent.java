package com.tycorp.tw.domain.event;

import com.tycorp.tw.domain.SubscriptionSlave;
import lombok.Getter;

import java.time.Instant;

@Getter
public class SubscriptionPermitsAddedEvent extends AbstractDomainEntityEvent {

    private SubscriptionSlave slave;
    private Long occuredAt = Instant.now().toEpochMilli();

    public SubscriptionPermitsAddedEvent(Object source, SubscriptionSlave slave) {
        super(source);
        this.slave = slave;
    }

}
