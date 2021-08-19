package com.tycorp.eb.rest.event;

import com.tycorp.eb.domain.SignedInUserDetail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;

@Getter
public class AbstractAppEvent extends ApplicationEvent {

    protected SignedInUserDetail signedInUserDetail;
    protected String UUID;

    public AbstractAppEvent(Object source) {
        super(source);
    }

}
