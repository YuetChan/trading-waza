package com.tycorp.eb.app;

import com.tycorp.eb.app.request.BasicRequestService;
import com.tycorp.eb.domain.subscription.repository.SubscriptionMasterRepository;
import com.tycorp.eb.domain.subscription.repository.SubscriptionSlaveRepository;
import com.tycorp.eb.domain.user.repository.EbUserRepository;
import com.tycorp.eb.spring_security.EbAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public abstract class AbstractEbAppAggregateService {

    @Autowired
    protected EbAuthenticationFacade authFacade;
    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    @Autowired
    protected BasicRequestService requestService;

    @Autowired
    protected SubscriptionMasterRepository masterRepo;
    @Autowired
    protected SubscriptionSlaveRepository slaveRepo;

    @Autowired
    protected EbUserRepository userRepo;

}
