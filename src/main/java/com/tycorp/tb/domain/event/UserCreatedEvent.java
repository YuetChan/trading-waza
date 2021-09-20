package com.tycorp.tb.domain.event;

import com.tycorp.tb.domain.User;
import lombok.Getter;

import java.time.Instant;

@Getter
public class UserCreatedEvent extends AbstractDomainEntityEvent {

    private User user;
    private Long occuredAt = Instant.now().toEpochMilli();

    public UserCreatedEvent(Object src, User user) {
        super(src);
        this.user = user;
    }

}