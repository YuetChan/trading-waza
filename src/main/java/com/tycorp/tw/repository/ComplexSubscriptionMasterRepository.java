package com.tycorp.tw.repository;

import com.tycorp.tw.domain.SubscriptionMaster;

public interface ComplexSubscriptionMasterRepository {

    Long DEFAULT_SUBSCRIPTION_MASTER_ID = 1l;
    SubscriptionMaster getDefaultSubscriptionMasters();

}
