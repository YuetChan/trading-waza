package com.tycorp.eb.repository;

import com.tycorp.eb.domain.SubscriptionMaster;
import org.springframework.beans.factory.annotation.Autowired;

public class ComplexSubscriptionMasterRepositoryImpl implements ComplexSubscriptionMasterRepository {

    @Autowired
    private SubscriptionMasterRepository masterRepo;

    @Override
    public SubscriptionMaster getDefaultSubscriptionMasters() {
        return masterRepo.findById(DEFAULT_SUBSCRIPTION_MASTER_ID).get();
    }

}
