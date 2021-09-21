package com.tycorp.tw.service;

import com.tycorp.tw.spring_security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public abstract class AbstractEntityService {

    @Autowired
    protected AuthenticationFacade authFacade;
    @Autowired
    protected ApplicationEventPublisher eventPublisher;

}
