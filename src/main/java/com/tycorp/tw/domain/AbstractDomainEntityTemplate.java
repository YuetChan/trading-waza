package com.tycorp.tw.domain;

import com.tycorp.tw.domain.event.AbstractDomainEntityEvent;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;

@Getter(AccessLevel.PRIVATE)
public abstract class AbstractDomainEntityTemplate {

    @Transient
    protected transient Collection<AbstractDomainEntityEvent> events = new ArrayList<>();

    @DomainEvents
    private Collection<AbstractDomainEntityEvent> publishEvents() {
        return getEvents();
    }
    @AfterDomainEventPublication
    private void onEventsPublished() {
        getEvents().clear();
    }

    public boolean containsEventType(Class event) {
        return getEvents().stream().anyMatch(e -> e.getClass().equals(event));
    }

    protected void registerEvent(AbstractDomainEntityEvent event) {
        if (!containsEventType(event.getClass())) {
            getEvents().add(event);
        }
    }

}
