package com.tycorp.eb.domain;

import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractEbDomainAggregateTemplate {

    @Transient
    protected transient Collection<AbstractEbDomainAggregateEvent> events = new ArrayList<>();

    @DomainEvents
    private Collection<AbstractEbDomainAggregateEvent> publishEvents() {
        return events;
    }
    public boolean containsEventType(Class event) {
        return events.stream().anyMatch(e -> e.getClass().equals(event));
    }

    protected void registerEvent(AbstractEbDomainAggregateEvent event) {
        if (!containsEventType(event.getClass())) {
            events.add(event);
        }
    }

    @AfterDomainEventPublication
    private void onEventsPublished() {
        events.clear();
    }

}
