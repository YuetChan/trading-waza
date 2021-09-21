package com.tycorp.tw.domain.event;

import com.tycorp.tw.domain.User;
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
