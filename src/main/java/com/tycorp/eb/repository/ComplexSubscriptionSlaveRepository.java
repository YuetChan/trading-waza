package com.tycorp.eb.repository;

import com.tycorp.eb.domain.SubscriptionSlave;

import java.util.Set;

public interface ComplexSubscriptionSlaveRepository {

    Set<SubscriptionSlave> getDefaultSubscriptionSlaves();

}
