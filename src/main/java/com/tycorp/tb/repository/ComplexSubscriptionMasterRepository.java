package com.tycorp.tb.repository;

import com.tycorp.tb.domain.SubscriptionMaster;

public interface ComplexSubscriptionMasterRepository {

    Long DEFAULT_SUBSCRIPTION_MASTER_ID = 1l;
    SubscriptionMaster getDefaultSubscriptionMasters();

}
