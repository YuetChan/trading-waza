package com.tycorp.eb.app;

import com.tycorp.eb.domain.user.model.LoginedEbUserDetail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
public class AbstractEbAppAggregateEvent extends ApplicationEvent {

    protected LoginedEbUserDetail loginedEbUserDetail;
    protected String UUID;

    public AbstractEbAppAggregateEvent(Object source) {
        super(source);
    }
    protected static Long generateOccuredAt() {
        return Instant.now().toEpochMilli();
    }

}
