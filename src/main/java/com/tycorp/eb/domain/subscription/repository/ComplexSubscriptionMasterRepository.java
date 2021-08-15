package com.tycorp.eb.domain.subscription.repository;

import com.tycorp.eb.domain.subscription.model.SubscriptionMaster;

import java.util.Set;

public interface ComplexSubscriptionMasterRepository {

    Set<SubscriptionMaster> getDefaultSubscriptionMasters();

}
