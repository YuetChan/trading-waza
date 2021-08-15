package com.tycorp.eb.domain.subscription.event;

import com.tycorp.eb.domain.AbstractEbDomainAggregateEvent;
import com.tycorp.eb.domain.subscription.model.SubscriptionSlave;
import lombok.Getter;

@Getter
public class SubscriptionExtendedEvent extends AbstractEbDomainAggregateEvent {

    private SubscriptionSlave slave;
    private Long occuredAt = generateOccuredAt();

    public SubscriptionExtendedEvent(Object source, SubscriptionSlave slave) {
        super(source);
        this.slave = slave;
    }

}
