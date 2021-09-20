package com.tycorp.tb.repository;

import com.tycorp.tb.domain.SubscriptionMaster;
import com.tycorp.tb.domain.SubscriptionSlave;

public interface ComplexSubscriptionSlaveRepository {

    SubscriptionSlave getDefaultSubscriptionSlave(SubscriptionMaster master);

}
