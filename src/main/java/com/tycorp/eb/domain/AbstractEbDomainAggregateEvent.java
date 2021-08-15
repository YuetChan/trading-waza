package com.tycorp.eb.domain;


import com.tycorp.eb.domain.user.model.LoginedEbUserDetail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;

@Getter
public abstract class AbstractEbDomainAggregateEvent extends ApplicationEvent {

    protected LoginedEbUserDetail loginedEbUserDetail;

    public AbstractEbDomainAggregateEvent(Object source) {
        super(source);
    }
    protected static Long generateOccuredAt() {
        return Instant.now().toEpochMilli();
    }

}
