package com.tycorp.eb.domain;

import com.tycorp.eb.domain.event.AbstractDomainEntityEvent;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractDomainEntityTemplate {

    @Transient
    protected transient Collection<AbstractDomainEntityEvent> events = new ArrayList<>();

    @DomainEvents
    private Collection<AbstractDomainEntityEvent> publishEvents() {
        return events;
    }
    @AfterDomainEventPublication
    private void onEventsPublished() {
        events.clear();
    }

    public boolean containsEventType(Class event) {
        return events.stream().anyMatch(e -> e.getClass().equals(event));
    }

    protected void registerEvent(AbstractDomainEntityEvent event) {
        if (!containsEventType(event.getClass())) {
            events.add(event);
        }
    }

}
