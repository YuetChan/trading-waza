package com.tycorp.eb.repository;

import com.tycorp.eb.domain.SubscriptionMaster;

import java.util.Set;

public interface ComplexSubscriptionMasterRepository {

    Long DEFAULT_SUBSCRIPTION_MASTER_ID = 1l;
    Set<SubscriptionMaster> getDefaultSubscriptionMasters();

}
