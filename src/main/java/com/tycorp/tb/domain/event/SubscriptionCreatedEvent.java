package com.tycorp.tb.domain.event;

import com.tycorp.tb.domain.SubscriptionSlave;
import lombok.Getter;

import java.time.Instant;

@Getter
public class SubscriptionCreatedEvent extends AbstractDomainEntityEvent {

    private SubscriptionSlave slave;
    private Long occuredAt = Instant.now().toEpochMilli();

    public SubscriptionCreatedEvent(Object source, SubscriptionSlave slave) {
        super(source);
        this.slave = slave;
    }

}
