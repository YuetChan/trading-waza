package com.tycorp.eb.domain.subscription.repository;

import com.tycorp.eb.domain.subscription.model.SubscriptionSlave;

import java.util.Set;

public interface ComplexSubscriptionSlaveRepository {

    Set<SubscriptionSlave> getDefaultSubscriptions();

}
