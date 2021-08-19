package com.tycorp.eb.domain.event;

import com.tycorp.eb.domain.Post;
import lombok.Getter;

import java.time.Instant;

@Getter
public class PostCreatedEvent extends AbstractDomainEntityEvent {

    private Post post;
    private Long occuredAt = Instant.now().toEpochMilli();

    public PostCreatedEvent(Object source, Post post) {
        super(source);
        this.post = post;
    }

}
