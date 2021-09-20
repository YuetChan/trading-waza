package com.tycorp.tb.domain.event;


import com.tycorp.tb.domain.SignedInUserDetail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class AbstractDomainEntityEvent extends ApplicationEvent {

    protected SignedInUserDetail signedInUserDetail;

    public AbstractDomainEntityEvent(Object source) {
        super(source);
    }

}
