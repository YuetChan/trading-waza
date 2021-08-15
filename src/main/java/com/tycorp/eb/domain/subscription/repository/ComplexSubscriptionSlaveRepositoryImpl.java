package com.tycorp.eb.domain.subscription.repository;

import com.tycorp.eb.domain.subscription.model.SubscriptionSlave;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

public class ComplexSubscriptionSlaveRepositoryImpl implements ComplexSubscriptionSlaveRepository {

    @Autowired
    private SubscriptionMasterRepository masterRepo;

    @Override
    public Set<SubscriptionSlave> getDefaultSubscriptions() {
        return masterRepo.getDefaultSubscriptionMasters()
                .stream().map(master -> new SubscriptionSlave(master)).collect(Collectors.toSet());
    }

}
