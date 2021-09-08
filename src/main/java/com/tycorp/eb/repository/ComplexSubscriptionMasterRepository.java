package com.tycorp.eb.repository;

import com.tycorp.eb.domain.SubscriptionMaster;

import java.util.Set;

public interface ComplexSubscriptionMasterRepository {

    Set<SubscriptionMaster> getDefaultSubscriptionMasters();

}
