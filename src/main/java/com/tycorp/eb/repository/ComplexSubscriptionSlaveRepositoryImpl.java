package com.tycorp.eb.repository;

import com.tycorp.eb.domain.SubscriptionMaster;
import com.tycorp.eb.domain.SubscriptionPermitEnum;
import com.tycorp.eb.domain.SubscriptionSlave;
import com.tycorp.eb.spring_security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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
