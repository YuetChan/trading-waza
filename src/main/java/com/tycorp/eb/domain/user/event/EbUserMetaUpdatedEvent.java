package com.tycorp.eb.domain.user.event;

import com.tycorp.eb.domain.AbstractEbDomainAggregateEvent;
import com.tycorp.eb.domain.user.model.EbUser;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EbUserMetaUpdatedEvent extends AbstractEbDomainAggregateEvent {

    private EbUser ebUser;
    private Long updatedAt = generateOccuredAt();

    public EbUserMetaUpdatedEvent(Object src, EbUser ebUser) {
        super(src);
        this.ebUser = ebUser;
    }

}
