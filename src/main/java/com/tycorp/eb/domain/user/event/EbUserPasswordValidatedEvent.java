package com.tycorp.eb.domain.user.event;

import com.tycorp.eb.domain.AbstractEbDomainAggregateEvent;
import com.tycorp.eb.domain.user.model.EbUser;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EbUserPasswordValidatedEvent extends AbstractEbDomainAggregateEvent {

    private EbUser ebUser;
    private Long updatedAt = generateOccuredAt();

    public EbUserPasswordValidatedEvent(Object src, EbUser ebUser) {
        super(src);
        this.ebUser = ebUser;
    }

}
