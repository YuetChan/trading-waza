package com.tycorp.tb.repository;

import com.tycorp.tb.domain.SubscriptionMaster;
import com.tycorp.tb.domain.SubscriptionPermitEnum;
import com.tycorp.tb.domain.SubscriptionSlave;
import com.tycorp.tb.spring_security.AuthenticationFacade;
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
