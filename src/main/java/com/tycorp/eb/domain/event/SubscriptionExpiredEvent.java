package com.tycorp.eb.domain.event;

import com.tycorp.eb.domain.SubscriptionSlave;
import lombok.Getter;

import java.time.Instant;

@Getter
public class SubscriptionExpiredEvent extends AbstractDomainEntityEvent {

    private SubscriptionSlave slave;
    private Long occuredAt = Instant.now().toEpochMilli();

    public SubscriptionExpiredEvent(Object source, SubscriptionSlave slave) {
        super(source);
        this.slave = slave;
    }

}
