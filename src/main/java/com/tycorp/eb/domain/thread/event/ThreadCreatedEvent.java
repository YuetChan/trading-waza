package com.tycorp.eb.domain.thread.event;

import com.tycorp.eb.domain.AbstractEbDomainAggregateEvent;
import com.tycorp.eb.domain.thread.model.Thread;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ThreadCreatedEvent extends AbstractEbDomainAggregateEvent {

    private Thread thread;
    private Long occuredAt = generateOccuredAt();

    public ThreadCreatedEvent(Object source, Thread thread) {
        super(source);
        this.thread = thread;
    }

}
