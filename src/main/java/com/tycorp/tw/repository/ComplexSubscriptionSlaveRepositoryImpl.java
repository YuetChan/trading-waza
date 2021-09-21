package com.tycorp.tw.repository;

import com.tycorp.tw.domain.SubscriptionMaster;
import com.tycorp.tw.domain.SubscriptionPermitEnum;
import com.tycorp.tw.domain.SubscriptionSlave;
import com.tycorp.tw.spring_security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class ComplexSubscriptionSlaveRepositoryImpl implements ComplexSubscriptionSlaveRepository {

    @Autowired
    private AuthenticationFacade authFacade;

    @Autowired
    private SubscriptionMasterRepository masterRepo;

    @Override
    public SubscriptionSlave getDefaultSubscriptionSlave(SubscriptionMaster master) {
        SubscriptionSlave defaultSlave = new SubscriptionSlave(master);
        defaultSlave.setOperator(authFacade.getDefaultAuthenticatedUserDetail());
        defaultSlave.extend(99999);
        defaultSlave.addPermits(Arrays.asList(
                SubscriptionPermitEnum.CREATE_THREAD, SubscriptionPermitEnum.UPDATE_THREAD));

        return defaultSlave;
    }

}
