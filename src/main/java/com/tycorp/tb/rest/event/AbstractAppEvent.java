package com.tycorp.tb.rest.event;

import com.tycorp.tb.domain.SignedInUserDetail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AbstractAppEvent extends ApplicationEvent {

    protected SignedInUserDetail signedInUserDetail;
    protected String UUID;

    public AbstractAppEvent(Object source) {
        super(source);
    }

}
