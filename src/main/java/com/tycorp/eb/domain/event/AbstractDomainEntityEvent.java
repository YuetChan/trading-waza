package com.tycorp.eb.domain.event;


import com.tycorp.eb.domain.SignedInUserDetail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;

@Getter
public abstract class AbstractDomainEntityEvent extends ApplicationEvent {

    protected SignedInUserDetail signedInUserDetail;

    public AbstractDomainEntityEvent(Object source) {
        super(source);
    }

}
